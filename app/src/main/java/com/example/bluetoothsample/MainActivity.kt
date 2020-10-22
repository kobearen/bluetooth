package com.example.bluetoothsample

import BluetoothConnect
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.net.wifi.p2p.WifiP2pDevice.CONNECTED
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


var mBluetoothLeScanner: BluetoothLeScanner? = null
var mScanCallback: ScanCallback? = null
var bluetoothGatt: BluetoothGatt? = null
var state : Int? = null
val peripherals = MutableLiveData<MutableList<Peripheral>>()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Fragmentを生成するためのFragmentTransactionの準備をして
        val firstFragment = BluetoothConnect()
        // FirstFragment.javaのonCreateView()がスタート
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // add()でフラグメントの追加、 add()の第一引数には、Fragmentの表示を行う場所のid、第二引数は表示したいFragmentを指定
        fragmentTransaction.add(R.id.fragment_container, firstFragment)
        // commit()で反映を行う
        fragmentTransaction.commit()

        val webView: WebView = findViewById(R.id.webView)
        webView.loadUrl("https://tanukigolf.com/") //https://www.google.co.jp/
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

        button.setOnClickListener{
            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

            // Bluetoothサポートしているかのチェック
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth未サポート", Toast.LENGTH_SHORT)
                finish()
            }

            mBluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner

            mScanCallback = initCallbacks()
            // スキャンの開始
            mBluetoothLeScanner?.startScan(mScanCallback)

        }
    }

    private fun initCallbacks(): ScanCallback? {
        return object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)

                // デバイスが見つかった！
                if (result?.device != null) {
                    // リストに追加などなどの処理をおこなう
                    //addDevice(result.getDevice(), result.getRssi())
                    addPeripheral(Peripheral(result))
                    println("リスト〜listOf(result)${(listOf(result))}")
                    connect(result.device)

                }

                // スキャンの停止
                mBluetoothLeScanner?.stopScan(mScanCallback)
                return
            }
        }
    }
    fun addPeripheral(peripheral: Peripheral) = addPeripherals(listOf(peripheral))

    fun addPeripherals(peripheralList: List<Peripheral>) {
        peripherals.value?.let { list ->
            peripheralList.forEach { peripheral ->
                val index = list.indexOfFirst { it.address == peripheral.address }
                if (index == -1) {
                    list.add(peripheral)
                } else {
                    list[index] = peripheral
                }
            }
            peripherals.postValue(list)
        }
    }

    fun connect(device: BluetoothDevice) {
        device.connectGatt(this, false, gattCallback)

    }
    private val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                // ペリフェラルとの接続に成功した時点でサービスを検索する
                gatt.discoverServices()
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                // ペリフェラルとの接続が切れた時点でオブジェクトを空にする
                if (bluetoothGatt != null) {
                    bluetoothGatt!!.close()
                    bluetoothGatt = null
//                }
//                state = DISCONNECTED
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                bluetoothGatt = gatt
                state = CONNECTED
            }
        }
//        fun registerNotification() {
//            // notificationが有効なBluetoothGattCharacteristicを取得する
//            val BluetoothGattCharacteristic: characteristic = findCharacteristic(serviceUUID, characteristicUUID, BluetoothGattCharacteristic.PROPERTY_NOTIFY)
//
//            // ペリフェラルのnotificationを有効化する。下のUUIDはCharacteristic Configuration Descriptor UUIDというもの
//            val BluetoothGattDescriptor: descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
//
//            // Androidフレームワークに対してnotification通知登録を行う, falseだと解除する
//            bluetoothGatt?.setCharacteristicNotification(characteristic, true)
//
//            // characteristic のnotification 有効化する
//            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
//            bluetoothGatt?.writeDescriptor(descriptor)
//        }

//        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic ) {
//            if (NOTIFICATION_CHARACTERISTIC_UUID.equals(characteristic.uuid.toString())) {
//                val notification_data = characteristic.value
//            }
//        }
    }
}



data class Peripheral(
    val localName: String?,
    val address: String,
    val rssi: Int,
    val serviceUuid: String?,
    val bluetoothDevice: BluetoothDevice?  = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        requireNotNull(parcel.readString()),
        requireNotNull(parcel.readInt()),
        parcel.readString(),
        parcel.readParcelable(BluetoothDevice::class.java.classLoader)
    )
    constructor(scanResult: ScanResult) : this(
        scanResult.device.name ?: "No Name",
        scanResult.device.address,
        scanResult.rssi,
        scanResult.scanRecord?.serviceUuids?.firstOrNull()?.uuid?.toString(),
        scanResult.device
    )

    var rssiString: String = "${rssi}dbm"

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(localName)
        parcel.writeString(address)
        parcel.writeInt(rssi)
        parcel.writeString(serviceUuid)
        parcel.writeParcelable(bluetoothDevice, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Peripheral> {
        override fun createFromParcel(parcel: Parcel): Peripheral = Peripheral(parcel)
        override fun newArray(size: Int): Array<Peripheral?> = arrayOfNulls(size)
    }
}
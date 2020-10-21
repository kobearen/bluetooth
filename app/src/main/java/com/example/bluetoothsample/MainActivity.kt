package com.example.bluetoothsample

import BluetoothConnect
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.net.wifi.p2p.WifiP2pDevice.CONNECTED
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


var mBluetoothLeScanner: BluetoothLeScanner? = null
var mScanCallback: ScanCallback? = null
var bluetoothGatt: BluetoothGatt? = null
var state : Int? = null

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

        button.setOnClickListener{
            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

            // Bluetoothサポートしているかのチェック
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth未サポート", Toast.LENGTH_SHORT)
                finish()
            }

            mBluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
            // kotlin プロパティ優先なのでメソッド使うな

            mScanCallback = initCallbacks()
            println(mScanCallback.toString())
            println("mScanCallback")

            // スキャンの開始
            mBluetoothLeScanner?.startScan(mScanCallback)

            // スキャンの停止
            // mBluetoothLeScanner?.stopScan(mScanCallback)

        }
    }

    private fun initCallbacks(): ScanCallback? {
        return object : ScanCallback() {
            override fun onScanResult(
                callbackType: Int,
                result: ScanResult
            ) {
                super.onScanResult(callbackType, result)

                // デバイスが見つかった！
                if (result != null && result.device != null) {
                    // リストに追加などなどの処理をおこなう
                    //addDevice(result.getDevice(), result.getRssi())
                    connect(result.device)

                }
                return
            }
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
        fun registerNotification() {
            // notificationが有効なBluetoothGattCharacteristicを取得する
            val BluetoothGattCharacteristic: characteristic = findCharacteristic(serviceUUID, characteristicUUID, BluetoothGattCharacteristic.PROPERTY_NOTIFY)

            // ペリフェラルのnotificationを有効化する。下のUUIDはCharacteristic Configuration Descriptor UUIDというもの
            val BluetoothGattDescriptor: descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))

            // Androidフレームワークに対してnotification通知登録を行う, falseだと解除する
            bluetoothGatt?.setCharacteristicNotification(characteristic, true)

            // characteristic のnotification 有効化する
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            bluetoothGatt?.writeDescriptor(descriptor)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic ) {
            if (NOTIFICATION_CHARACTERISTIC_UUID.equals(characteristic.uuid.toString())) {
                val notification_data = characteristic.value
            }
        }
    }
}

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.experimental.and


class MainActivity : AppCompatActivity() {
    //LOG
    private val TAG = "DEVICE_INFO"

    //BLE
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBleGatt: BluetoothGatt? = null
    private var mBluetoothGattCharacteristic: BluetoothGattCharacteristic? = null
    private var RecvEditText: EditText? = null

    //
    val handler: Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.bluetoothsample.R.layout.activity_main)

        RecvEditText = findViewById<View>(R.id.text1) as EditText
        InitializeBleSetting()

        button.setOnClickListener {
            mBluetoothAdapter!!.bluetoothLeScanner.startScan(mScanCallback)
        }
    }

    /**
     * BLEの初期設定をおこなうところ
     */
    @SuppressLint("WrongConstant")
    private fun InitializeBleSetting() {
        //BLEがサポートしているかの確認
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "お使いの端末はBLEが対応していません", Toast.LENGTH_SHORT).show()
            finish()
        }

        //bluetoothがONになっているか確認
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        //permisstion
        if (PermissionChecker.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocatePermission()
            return
        }
    }

    /**
     * Permisstionの許可をする関数
     */
    private fun requestLocatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            GestureDescription.Builder(this)
//                    .setTitle("パーミッションの追加説明")
//                    .setMessage("このアプリを使うには位置情報の許可が必要です")
//                    .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
//                        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                                REQUEST_CODE_LOCATE_PERMISSION)
//                    })
//                    .create()
//                    .show()
            return
        }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATE_PERMISSION)
        return
    }

    /**
     * Buttonイベント
     * @param
     */


//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.ConectButton -> if (mBleGatt == null) ConnectBleDevice()
//            R.id.DisConectButton -> if (mBleGatt != null) DisConnectDevice()
//        }
//    }

    /**
     * ConnctBleDevice
     */
    private fun ConnectBleDevice() {
        mBluetoothAdapter!!.bluetoothLeScanner.startScan(mScanCallback)
    }

    /**
     * DisConncetDevice
     */
    private fun DisConnectDevice() {
        mBleGatt!!.close()
        mBleGatt = null
        Toast.makeText(this, "切断", Toast.LENGTH_SHORT).show()
    }

    /**
     * callback
     * ScanCallback
     * BLEの探索
     */
    private val mScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
//            Log.i(TAG, "onScanResult()")
//            Log.i(TAG, "DeviceName:" + result.getDevice().getName())
//            Log.i(TAG, "DeviceAddr:" + result.getDevice().getAddress())
//            Log.i(TAG, "RSSI:" + result.getRssi())
//            Log.i(TAG, "UUID:" + result.getScanRecord().getServiceUuids())

            //接続するPeripheralNameが見つかったら接続
            if (PERIPHERAL_NAME.equals(result.getDevice().getName(), ignoreCase = true)) {
                mBluetoothAdapter!!.bluetoothLeScanner.stopScan(this) //探索を停止
                result.getDevice().connectGatt(this@MainActivity, false, mGattCallback)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
        }
    }

    /**
     * CallBack
     * GATTの処理関係
     * Peripheralへの接続,切断,データのやりとり
     */
    private val mGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // 接続できたらサービスの検索
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) { //マイコンの応答がなくなった時の処理
                mBleGatt!!.close()
                mBleGatt = null
                handler.post(Runnable { Toast.makeText(this@MainActivity, "切断されました", Toast.LENGTH_SHORT).show() })
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt.getService(UUID.fromString(CUSTOM_SURVICE_UUID))
                if (service != null) {
                    mBleGatt = gatt

                    //Notifyの接続を試みてる
                    mBluetoothGattCharacteristic = service.getCharacteristic(UUID.fromString(CUSTOM_CHARACTERSTIC_UUID))
                    if (mBluetoothGattCharacteristic != null) {
                        val registered = gatt.setCharacteristicNotification(mBluetoothGattCharacteristic, true)
                        val descriptor = mBluetoothGattCharacteristic!!.getDescriptor(UUID.fromString(ANDROID_CENTRAL_UUID))
                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        mBleGatt!!.writeDescriptor(descriptor)
                        if (registered) {
                            Log.e("INFO", "notify ok")
                            handler.post(Runnable { Toast.makeText(this@MainActivity, "接続", Toast.LENGTH_SHORT).show() })
                        } else Log.e("INFO", "notify ng")
                    }
                }
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)

            //回避:android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
            handler.post(Runnable { //順文字列を10進数にしている
                val RecvByteValue = characteristic.value
                var RecvStrValue = ""
                for (i in RecvByteValue.indices) {
                    RecvStrValue += (RecvByteValue[i] and 0xff.toByte()).toString() + ","
                }
                RecvEditText!!.setText(RecvStrValue)
            })
        }
    }

    companion object {
        //Permission
        private const val REQUEST_ENABLE_BT = 1
        private const val REQUEST_CODE_LOCATE_PERMISSION = 5

        //PERIPHERAL_NAME
        private const val PERIPHERAL_NAME = "MyBlePeripheral"

        //UUID
        private const val CUSTOM_SURVICE_UUID = "713d0000-503e-4c75-ba94-3148f18d941e"
        private const val CUSTOM_CHARACTERSTIC_UUID = "713d0001-503e-4c75-ba94-3148f18d941e"

        //Androidの固定値
        private const val ANDROID_CENTRAL_UUID = "00002902-0000-1000-8000-00805f9b34fb"
    }
}
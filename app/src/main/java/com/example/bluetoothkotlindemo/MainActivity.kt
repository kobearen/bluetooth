package com.example.bluetoothkotlindemo

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

const val DEVICE_NAME = "com.example.bluetoothkotlindemo.DEVICE_NAME"
const val DEVICE_ADDRESS = "com.example.bluetoothkotlindemo.DEVICE_ADDRESS"
const val NETWORK_ADDRESS = "https://tanukigolf.com/"
//https://www.google.co.jp/

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnOpenActivity = findViewById<Button>(R.id.button)
        btnOpenActivity.setOnClickListener {
            startActivity(Intent(this, DeviceConnectionActivity::class.java))
        }

        val webView: WebView = findViewById(R.id.webView)
        webView.apply {
            loadUrl(NETWORK_ADDRESS)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
        }


//        // BLEがサポートされているかチェック。
//        if (!checkSupported()) finish()
//        Log.d("wedede","deff")
//
//        // 必要な権限が許可されているかチェック。
//        val hasPermission = checkPermission();
//        mTxtPermission = findViewById<TextView>(R.id.txtPermission)
//        mTxtPermission.setText(if (hasPermission) R.string.has_permission else R.string.no_permission);
//
//        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
//        mBluetoothAdapter = bluetoothManager.adapter
//
//        // Bluetoothが有効かチェック。
//        val bluetoothEnabled = checkBluetoothEnabled()
//        mTxtEnabled = findViewById<TextView>(R.id.txtEnabled)
//        mTxtEnabled.setText(if (bluetoothEnabled) R.string.bt_enabled else R.string.bt_not_enabled);
//
//        // ペアリング済みデバイスの一覧を表示。
//        mLstDevices = findViewById(R.id.lstDevices)
//        val devices = mBluetoothAdapter.bondedDevices.toList()
//        val deviceNames: List<String> = devices.map { "${it.name} (${it.address})" }
//        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceNames)
//        mLstDevices.adapter = adapter
//
//        // ペアリング済みデバイス一覧の行がクリックされたときの処理。
//        mLstDevices.setOnItemClickListener { parent, view, position, id ->
//            val device = devices.get(position)
//            if (device != null) {
//                // デバイス詳細情報画面を開く。
//                val intent = Intent(this, DeviceDetailActivity::class.java).apply {
//                    putExtra(DEVICE_NAME, device.name)
//                    putExtra(DEVICE_ADDRESS, device.address)
//                }
//                startActivity(intent)
//            }
//        }
//
//        if (devices.isEmpty()) {
//            findViewById<TextView>(R.id.txtDeviceListMessage).setText(R.string.devices_message_none)
//        }
//
//        // OSのBluetooth設定画面を開く。
//        val btnOpenBlutoothSettings = findViewById<Button>(R.id.btnOpenBlutoothSettings)
//        btnOpenBlutoothSettings.setOnClickListener {
//            // startActivity(Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS))
//        }
//
//        // デバイス検出画面を開く。
//        val btnOpenScanActivity = findViewById<Button>(R.id.btnOpenScanActivity)
//        btnOpenScanActivity.setOnClickListener {
//            startActivity(Intent(this, DeviceScanActivity::class.java))
//        }

    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        Log.d("Debug", "requestCode=${requestCode}, resultCode=${resultCode}")
//
//        if (REQUEST_BT_ON == requestCode) {
//            mTxtEnabled.setText(if (resultCode == RESULT_OK) R.string.bt_enabled else R.string.bt_not_enabled)
//        }
//    }
//
//    fun checkSupported(): Boolean {
//        // Use this check to determine whether BLE is supported on the device.
//        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        Log.d("Debug", "BLEがサポートされています。")
//        return true
//    }
//
//    fun checkPermission(): Boolean {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("Debug", "実行時の権限チェック：許可なし")
//
//            // ユーザーに権限を要求するダイアログを表示済みかどうかチェック。
//            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                Log.d("Debug", "明示的に不許可済")
//                requestPermissions(
//                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
//                    REQUEST_ALLOW_PERMISSION
//                )
//            } else {
//                Log.d("Debug", "まだ許可も不許可もしていない")
//                // 明示的に許可も不許可もされていなければ、ユーザーに許可を求める。
//                requestPermissions(
//                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
//                    REQUEST_ALLOW_PERMISSION
//                )
//            }
//            // 処理を中断。明示的に許可した後アプリを再起動してもらう。
//            return false
//        }
//
//        Log.d("Debug", "明示的に許可済み")
//        return  true
//    }
//
//    fun checkBluetoothEnabled(): Boolean {
//        if (mBluetoothAdapter == null) return false
//
//        // 有効でなければ有効にする
//        if (mBluetoothAdapter.isEnabled == false) {
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableBtIntent, REQUEST_BT_ON)
//            return false
//        }
//
//        return true
//    }
}


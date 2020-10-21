package com.example.bluetoothsample

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


var mBluetoothLeScanner: BluetoothLeScanner? = null
var mScanCallback: ScanCallback? = null

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    //addDevice(result.getDevice(), result.getRssi());
                    println(result.getDevice())
                    println(result.getRssi())
                }
                return
            }
        }
    }
}

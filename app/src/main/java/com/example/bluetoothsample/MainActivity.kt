package com.example.bluetoothsample

import BluetoothConnect
import android.Manifest
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


//BLE
private var mBluetoothAdapter: BluetoothAdapter? = null
private val mBleGatt: BluetoothGatt? = null
private val mBluetoothGattCharacteristic: BluetoothGattCharacteristic? = null

//ACTION_FOUNDのBroadcastReceiverを作成
val receiver = object : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action: String? = intent.action
        when(action) {
            BluetoothDevice.ACTION_FOUND -> {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address

                Log.d("Debug", "Found ${device.type} ${device.name} ${device.address}")

                // 目的のデバイスが見つかった場合にそのデバイスを操作する処理
                //
            }
        }
    }
}

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

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        val context = this

        button.setOnClickListener{

            // 実行時に権限をチェック。
            if (ContextCompat.checkSelfPermission(button.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug", "実行時の権限チェック：許可なし")

                // ユーザーに権限を要求するダイアログを表示済みかどうかチェック。
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Log.d("Debug", "明示的に不許可済")
                    // PEND: 本来はここでユーザーに権限が必要な理由を説明する画面を表示する。
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0 )
                } else {
                    Log.d("Debug", "まだ許可も不許可もしていない")
                    // 明示的に許可も不許可もされていなければ、ユーザーに許可を求める。
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0 )
                }
                // 処理を中断。明示的に許可した後再度ボタンを押してもらう。
                return@setOnClickListener
            }

            Log.d("Debug", "明示的に許可済")  // 下の処理を続行。

            // Get the default adapter
            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
            // BluetoothAdapter を取得
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
                return@setOnClickListener
            }

            // Bluetooth を有効にする
            if (bluetoothAdapter.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 0)
            }

            // Bluetoothデバイスのスキャンを開始する。 *** これがないとBroadcastReceiverのonReceiveは呼ばれません。
            bluetoothAdapter.startDiscovery()

            //***************************************** ↓↓↓ 以下はおそらく39行目以下に書いたほうが良いかも（?）
            var bluetoothHeadset: BluetoothHeadset? = null
            val profileListener = object : BluetoothProfile.ServiceListener {
                override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
                    if (profile == BluetoothProfile.HEADSET) {
                        bluetoothHeadset = proxy as BluetoothHeadset

                        // ... call functions on bluetoothHeadset

                        // Close proxy connection after use.
                        bluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET, bluetoothHeadset)
                    }
                }
                override fun onServiceDisconnected(profile: Int) {
                    if (profile == BluetoothProfile.HEADSET) {
                        bluetoothHeadset = null
                    }
                }
            }
            // プロキシへの接続を確立
            bluetoothAdapter.getProfileProxy(context, profileListener, BluetoothProfile.HEADSET)
            //***************************************** ↑↑↑

        }

        fun onDestroy() {
            super.onDestroy()
            // Don't forget to unregister the ACTION_FOUND receiver.
            unregisterReceiver(receiver)
        }
    }
}
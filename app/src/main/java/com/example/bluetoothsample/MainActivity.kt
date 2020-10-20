package com.example.bluetoothsample

import BluetoothConnect
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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



        button.setOnClickListener{
            var bluetoothHeadset: BluetoothHeadset? = null

// Get the default adapter
            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
            // BluetoothAdapter を取得
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
            }
            // Bluetooth を有効にする
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 0)
            }

            val profileListener = object : BluetoothProfile.ServiceListener {

                override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
                    if (profile == BluetoothProfile.HEADSET) {
                        bluetoothHeadset = proxy as BluetoothHeadset
                    }
                }

                override fun onServiceDisconnected(profile: Int) {
                    if (profile == BluetoothProfile.HEADSET) {
                        bluetoothHeadset = null
                    }
                }
            }

// プロキシへの接続を確立
            bluetoothAdapter?.getProfileProxy(this, profileListener, BluetoothProfile.HEADSET)

// ... call functions on bluetoothHeadset




            // Register for broadcasts when a device is discovered.
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver, filter)

            // Close proxy connection after use.
            bluetoothAdapter?.closeProfileProxy(BluetoothProfile.HEADSET, bluetoothHeadset)
        }



        fun onDestroy() {
            super.onDestroy()
            // Don't forget to unregister the ACTION_FOUND receiver.
            unregisterReceiver(receiver)
        }
    }
}

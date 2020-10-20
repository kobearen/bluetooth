package com.example.bluetoothsample

import BluetoothConnect
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    //  IntentFilterの設定。
    //  優先度を最大の999 (SYSTEM_HIGH_PRIORITY - 1) にしています。
    private val pairingRequestIntentFilter: IntentFilter by lazy {
        IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST).also {
            it.priority = IntentFilter.SYSTEM_HIGH_PRIORITY - 1
        }
    }

    //  BroadcastReceiver
    private var pairingBroadcastReceiver: PairingBroadcastReceiver? = null

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




    }

    override fun onResume() {
        super.onResume()

        //  ここでBroadcastReceiverの登録を行っています。
        this.pairingBroadcastReceiver = PairingBroadcastReceiver()
        this.registerReceiver(this.pairingBroadcastReceiver, this.pairingRequestIntentFilter)
    }

    override fun onPause() {
        super.onPause()

        //  ここでBroadcastReceiverの登録解除を行っています。
        this.unregisterReceiver(this.pairingBroadcastReceiver)
        this.pairingBroadcastReceiver = null
    }

    //  ペアリング //  Android標準の場合
    //  (this.adapter: BluetoothAdapter)
    private fun pair(address: String, pin: ByteArray) {
        this.pairingBroadcastReceiver!!.pin = pin
        val bluetoothDevice = this.adapter.getRemoteDevice(address)
        bluetoothDevice.createBond()
    }
}

class PairingBroadcastReceiver : BroadcastReceiver() {

    //  PINコードの受け渡し口
    //  (サンプルコードなので雑でも気にしないでね♡)
    var pin: ByteArray? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        if (intent.action == BluetoothDevice.ACTION_PAIRING_REQUEST) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)!!
            val pin = this.pin!!

            device.setPin(pin)

            //  他のBroadcastReceiverにメッセージが飛んでいかなないようにする。
            //  これを呼ばないとシステムのペアリング要求通知が出てきてしまう。
            this.abortBroadcast()
        }
    }
}
package com.example.bluetoothsample

import BluetoothConnect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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

    }
}

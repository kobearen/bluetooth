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

    }
    // 中身を空にする ⇒　戻るボタンが使えないようになる
    override fun onBackPressed() {
    }
}


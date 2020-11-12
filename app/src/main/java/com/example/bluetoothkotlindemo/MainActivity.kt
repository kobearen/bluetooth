package com.example.bluetoothkotlindemo

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

private lateinit var mBluetoothAdapter: BluetoothAdapter
const val DEVICE_NAME = "com.example.bluetoothkotlindemo.DEVICE_NAME"
const val DEVICE_ADDRESS = "com.example.bluetoothkotlindemo.DEVICE_ADDRESS"
const val NETWORK_ADDRESS = "https://kobearen.hatenablog.com/entry/login"
        //"https://tanukigolf.com/"
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


        // 内山先生に質問1 37と44は合体できない？
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

        // WebViewClientの設定
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                println("URLに遷移しようとしています。::$url\n\n")

                if(url!!.contains("task")) {
                    // URLに"task"文字が含まれているとき
                    // bluetooth接続状態を確認→webに送る
                    view?.loadUrl("https://tanukigolf.com/")//遷移

                    // ペアリング済みデバイスの一覧を表示
                    // DeviceConnectionActivity().getListPairedDevices()
                    return true        // 内山先生に質問2 trueは誰に何を返している？
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        }

    }
    // 中身を空にする ⇒　戻るボタンが使えないようになる
    override fun onBackPressed() {
    }
}


package xyz.betauto.otpreceiver

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

import android.webkit.ValueCallback
import androidx.annotation.RequiresApi
import android.webkit.WebSettings
import android.webkit.WebViewClient


class WebActivity : AppCompatActivity() {

    lateinit var web_view: WebView
     var marker1 = arrayOf(25.067426185279952, 121.55215103472267)
     var marker2 = arrayOf(25.00010396118553, 121.58172620121954)
     var centerpoint = arrayOf((marker1[0] + marker2[0])/2, (marker1[1] + marker2[1])/2)
     var markers = arrayOf(marker1, marker2)
    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acticite_webview);
        web_view = findViewById(R.id.webview);
        val webSettings: WebSettings = web_view.getSettings()
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webSettings.useWideViewPort = true// 关键点
        // webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.displayZoomControls = false
        webSettings.javaScriptEnabled = true // 设置支持javascript脚本
        webSettings.allowFileAccess = true // 允许访问文件
        webSettings.builtInZoomControls = true // 设置显示缩放按钮
        webSettings.setSupportZoom(true) // 支持缩放
        webSettings.blockNetworkImage = false
        webSettings.loadWithOverviewMode = true
        webSettings.domStorageEnabled = true
        web_view.loadUrl("file:///android_asset/maptest4.html");
        web_view.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {

            }
        }

    }
}
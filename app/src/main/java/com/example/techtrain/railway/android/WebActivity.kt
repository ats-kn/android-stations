package com.example.techtrain.railway.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // WebViewの設定
        val url = intent.getStringExtra(getString(R.string.textview_label_url))
        val webView = binding.webView
        webView.loadUrl(url!!)
    }
}

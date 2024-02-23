package com.example.techtrain.railway.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ViewBindingの設定
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TextViewの設定
        binding.textView.text = getString(R.string.main_text)

        //Buttonの設定(editTextが受け取ったテキストをtextViewで表示)
        binding.button.setOnClickListener {
            binding.textView.text = binding.editText.text
        }
    }
}

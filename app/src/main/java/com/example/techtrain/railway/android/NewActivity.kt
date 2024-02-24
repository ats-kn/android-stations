package com.example.techtrain.railway.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityNewBinding

class NewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        //Bindingの設定
        binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intentからeditTextのテキストを取得
        val editText = intent.getStringExtra("KEY_INPUT_TEXT")
        //textViewにeditTextのテキストを表示
        binding.textView.text = editText
    }
}

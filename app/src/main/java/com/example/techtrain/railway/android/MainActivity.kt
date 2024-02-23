package com.example.techtrain.railway.android

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TextViewの設定
        val textView = findViewById<TextView>(R.id.text)

        //EditTextの設定
        val editText = findViewById<EditText>(R.id.editText)

        //Buttonの設定
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            textView.text = editText.text
        }
    }
}

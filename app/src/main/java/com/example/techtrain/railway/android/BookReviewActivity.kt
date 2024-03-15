package com.example.techtrain.railway.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityMainBinding

class BookReviewActivity: AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Bindingの設定
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SingInボタンが押された時の処理
        binding.signInButton.setOnClickListener {
            //SignInActivityに遷移
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        //Loginボタンが押された時の処理
        binding.loginButton.setOnClickListener {
            //LoginActivityに遷移
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}

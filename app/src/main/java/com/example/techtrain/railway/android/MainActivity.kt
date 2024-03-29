package com.example.techtrain.railway.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // ViewBindingを使うための変数を定義
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bindingの設定
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferencesからトークンを取得
        val token =
            getSharedPreferences(
                getString(R.string.preference),
                MODE_PRIVATE,
            ).getString(getString(R.string.token_key), null)

        // トークンがnullでなければBookReviewActivityに遷移
        if (token != null) {
            val intent = Intent(this, BookReviewActivity::class.java)
            startActivity(intent)
            // MainActivityを終了
            finish()
        }

        // SingInボタンが押された時の処理
        binding.signInButton.setOnClickListener {
            // SignInActivityに遷移
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        // Loginボタンが押された時の処理
        binding.loginButton.setOnClickListener {
            // LoginActivityに遷移
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

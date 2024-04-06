package com.example.techtrain.railway.android.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityLoginBinding
import com.example.techtrain.railway.android.utils.UserInfoValidation
import com.example.techtrain.railway.android.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ツールバーに戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // バリテーションのためのTextWatcherを設定
        val textWatcher =
            UserInfoValidation.createTextWatcherUserInfo(
                this,
                null,
                binding.editTextEmail,
                binding.editTextPassword,
                binding.loginButton,
                isValidInput = { _, email, password ->
                    // emailとpasswordのバリデーション確認
                    UserInfoValidation.isValidEmail(email) &&
                    UserInfoValidation.isValidPassword(password)
                },
            )

        binding.editTextEmail.addTextChangedListener(textWatcher)
        binding.editTextPassword.addTextChangedListener(textWatcher)

        binding.loginButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            loginViewModel.login(email, password)

            loginViewModel.loginResult.observe(this) { result ->
                result.fold(
                    onSuccess = {
                        // BookReviewActivityに遷移
                        val intent = Intent(this, BookReviewActivity::class.java)
                        startActivity(intent)
                    },
                    onFailure = { exception ->
                        Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    // ツールバーのボタンが押されたときの処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // 戻るボタンが押されたときの処理
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

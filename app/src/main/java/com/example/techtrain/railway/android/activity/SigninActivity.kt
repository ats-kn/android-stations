package com.example.techtrain.railway.android.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.activity.BookReviewActivity
import com.example.techtrain.railway.android.databinding.ActivitySigninBinding
import com.example.techtrain.railway.android.utils.UserInfoValidation
import com.example.techtrain.railway.android.viewmodel.SigninViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private val signinViewModel: SigninViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Bindingの設定
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ツールバーに戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // バリデーションのためのTextWatcherを設定
        val textWatcher = UserInfoValidation.createTextWatcherUserInfo(
            this,
            binding.editTextName,
            binding.editTextEmail,
            binding.editTextPassword,
            binding.signinButton
        ) { name, email, password ->
            // name, email, passwordのバリデーション確認
            UserInfoValidation.isValidName(name) &&
            UserInfoValidation.isValidEmail(email) &&
            UserInfoValidation.isValidPassword(password)
        }

        binding.editTextName.addTextChangedListener(textWatcher)
        binding.editTextEmail.addTextChangedListener(textWatcher)
        binding.editTextPassword.addTextChangedListener(textWatcher)

        // サインインボタンが押された時の処理
        binding.signinButton.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            signinViewModel.signin(name, email, password)
        }

        // ViewModelの結果を観察
        signinViewModel.signinResult.observe(this) { result ->
            result.fold(
                onSuccess = {
                    // BookReviewActivityに遷移
                    val intent = Intent(this, BookReviewActivity::class.java)
                    // バックキーでサインイン画面に戻れないようにする
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                },
                onFailure = { exception ->
                    Toast.makeText(
                        this,
                        exception.message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            )
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

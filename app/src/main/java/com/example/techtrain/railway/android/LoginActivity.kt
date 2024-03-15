package com.example.techtrain.railway.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.data.User
import com.example.techtrain.railway.android.databinding.ActivityLoginBinding
import com.example.techtrain.railway.android.utils.ValidationUtils
import com.example.techtrain.railway.android.utils.service
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textWatcher = ValidationUtils.createTextWatcher(this,null, binding.editTextEmail, binding.editTextPassword, binding.loginButton)
        binding.editTextEmail.addTextChangedListener(textWatcher)
        binding.editTextPassword.addTextChangedListener(textWatcher)

        binding.loginButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val user = User("", email, password)
            val login = service.login(user)

            login.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Log.d("LoginActivity", "ログインに成功しました")

                        // サインインに成功したらトークンを取得
                        val token = response.body()?.string()?.substringAfter("token\":\"")?.substringBefore("\"")
                        // SharedPreferencesを使用してトークンを保存
                        val sharedPref = getSharedPreferences(
                            getString(R.string.signin_preference), Context.MODE_PRIVATE)
                        with (sharedPref.edit()) {
                            putString(getString(R.string.token_key), token)
                            apply()
                        }

                        // BookReviewActivityに遷移
                        val intent = Intent(this@LoginActivity, BookReviewActivity::class.java)
                        startActivity(intent)

                    } else {
                        // サーバからのhttpエラーメッセージを取得
                        val errorMessage = response.errorBody()?.string()
                        // errorMessageの中からErrorMessageJPを抜き出す
                        val errorMessageJP = errorMessage?.substringAfter("ErrorMessageJP\":\"")?.substringBefore("\"")
                        Toast.makeText(this@LoginActivity, "Error: $errorMessageJP", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

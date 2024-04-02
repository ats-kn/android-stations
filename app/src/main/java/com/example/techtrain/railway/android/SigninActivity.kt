package com.example.techtrain.railway.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.data.User
import com.example.techtrain.railway.android.databinding.ActivitySigninBinding
import com.example.techtrain.railway.android.utils.UserInfoValidation
import com.example.techtrain.railway.android.utils.service
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bindingの設定
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // バリテーションのためのTextWatcherを設定
        val textWatcher =
            UserInfoValidation.createTextWatcherUserInfo(
                this,
                binding.editTextName,
                binding.editTextEmail,
                binding.editTextPassword,
                binding.signinButton,
                isValidInput = { name, email, password ->
                    // name, email, passwordのバリデーション確認
                    UserInfoValidation.isValidName(
                        name,
                    ) &&
                        UserInfoValidation.isValidEmail(
                            email,
                        ) && UserInfoValidation.isValidPassword(password)
                },
            )

        binding.editTextName.addTextChangedListener(textWatcher)
        binding.editTextEmail.addTextChangedListener(textWatcher)
        binding.editTextPassword.addTextChangedListener(textWatcher)

        // サインインボタンが押された時の処理
        binding.signinButton.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val user = User(name, email, password)
            val signin = service.signin(user)

            // enqueue(非同期通信)でサーバからレスポンスを受け取る
            signin.enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        if (response.isSuccessful) {
                            Log.d("SignInActivity", "サインインに成功しました")

                            // サインインに成功したらトークンを取得
                            val token =
                                response.body()?.string()?.substringAfter(
                                    "token\":\"",
                                )?.substringBefore("\"")
                            // SharedPreferencesを使用してトークンを保存
                            val sharedPref =
                                getSharedPreferences(
                                    getString(R.string.preference),
                                    Context.MODE_PRIVATE,
                                )
                            with(sharedPref.edit()) {
                                putString(getString(R.string.token_key), token)
                                apply()
                            }

                            // BookReviewActivityに遷移
                            val intent = Intent(this@SigninActivity, BookReviewActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // サーバからのhttpエラーメッセージを取得
                            val errorMessage = response.errorBody()?.string()

                            // errorMessageの中からErrorMessageJPを抜き出す
                            val errorMessageJP =
                                errorMessage?.substringAfter(
                                    "ErrorMessageJP\":\"",
                                )?.substringBefore("\"")

                            Toast.makeText(
                                this@SigninActivity,
                                "Error: $errorMessageJP",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseBody>,
                        t: Throwable,
                    ) {
                        Toast.makeText(
                            this@SigninActivity,
                            getString(R.string.fail_network),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                },
            )
        }
    }
}

package com.example.techtrain.railway.android

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.data.User
import com.example.techtrain.railway.android.databinding.ActivitySigninBinding
import com.example.techtrain.railway.android.utils.ValidationUtils
import com.example.techtrain.railway.android.utils.service
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textWatcher = ValidationUtils.createTextWatcher(this, binding.editTextName, binding.editTextEmail, binding.editTextPassword, binding.signinButton)
        binding.editTextEmail.addTextChangedListener(textWatcher)
        binding.editTextPassword.addTextChangedListener(textWatcher)

        binding.signinButton.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val user = User(name, email, password)
            val signin = service.signin(user)

            signin.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Log.d("SignInActivity", "サインインに成功しました")
                    } else {
                        // サーバからのhttpエラーメッセージを取得
                        val errorMessage = response.errorBody()?.string()
                        // errorMessageの中からErrorMessageJPを抜き出す
                        val errorMessageJP = errorMessage?.substringAfter("ErrorMessageJP\":\"")?.substringBefore("\"")
                        Toast.makeText(this@SigninActivity, "Error: $errorMessageJP", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@SigninActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

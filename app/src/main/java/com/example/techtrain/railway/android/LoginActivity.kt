package com.example.techtrain.railway.android

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.data.Login
import com.example.techtrain.railway.android.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val user = Login(email, password)

            val login = service.login(user)

            login.enqueue(object: Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    if (response.isSuccessful) {
                        Log.d("LoginActivity", "ログインに成功しました")
                    } else {
                        //エラーを示すtoastを表示
                        Toast.makeText(this@LoginActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    //エラーを示すtoastを表示
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

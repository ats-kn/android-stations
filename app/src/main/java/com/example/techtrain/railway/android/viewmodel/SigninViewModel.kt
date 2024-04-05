package com.example.techtrain.railway.android.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.techtrain.railway.android.R
import com.example.techtrain.railway.android.data.User
import com.example.techtrain.railway.android.utils.service
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel

class SigninViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference),
        Context.MODE_PRIVATE
    )

    val signinResult = MutableLiveData<Result<String>>()

    fun signin(name: String, email: String, password: String) {
        val user = User(name, email, password)
        val signin = service.signin(user)

        signin.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val token = response.body()?.string()?.substringAfter("token\":\"")?.substringBefore("\"")

                    // SharedPreferencesを使用してトークンを保存
                    with(sharedPref.edit()) {
                        putString(context.getString(R.string.token_key), token?:"")
                        apply()
                    }

                    signinResult.value = Result.success(token?:"")
                } else {
                    val errorMessageJp = response.errorBody()?.string()?.substringAfter("message\":\"")?.substringBefore("\"")
                    signinResult.value = Result.failure(Exception(errorMessageJp))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                signinResult.value = Result.failure(t)
            }
        })
    }
}

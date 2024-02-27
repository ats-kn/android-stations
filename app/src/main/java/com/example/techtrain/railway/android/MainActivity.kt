package com.example.techtrain.railway.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Bindingの設定
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //APIからデータを取得
        val call = service.getBookData()

        //enqueue(非同期通信)でデータを取得
        call.enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    // GETしたデータをtextViewに表示
                    binding.textView.text = data.toString()
                }
            }

            //通信に失敗した場合
            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                //エラーをログに表示
                Log.d("Error", t.message.toString())
            }
        })
    }
}

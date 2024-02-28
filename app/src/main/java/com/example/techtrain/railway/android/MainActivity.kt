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

        //APIのインスタンスを取得
        val getBookData = service.getBookData()

        //enqueue(非同期通信)でデータを取得
        getBookData.enqueue(object : Callback<List<Book>> {
            override fun onResponse(
                call: Call<List<Book>>,
                response: Response<List<Book>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    // GETしたデータをtextViewに表示
                    binding.textView.text = data.toString()
                } else {
                    //httpステータスコードが200番台以外の場合、ログを表示
                    Log.e(getString(R.string.mainactivity), response.message())
                }
            }

            //通信に失敗した場合
            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                //エラーをログに表示
                Log.e(getString(R.string.mainactivity), t.message.toString())
            }
        })
    }
}

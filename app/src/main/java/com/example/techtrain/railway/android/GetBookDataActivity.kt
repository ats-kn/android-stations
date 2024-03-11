package com.example.techtrain.railway.android

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.databinding.ActivityGetbookdataBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class GetBookDataActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGetbookdataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getbookdata)

        //Bindingの設定
        binding = ActivityGetbookdataBinding.inflate(layoutInflater)
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

                    //GETしたデータをRecyclerViewに表示
                    runOnUiThread {
                        //Loadingテキストを非表示にする
                        binding.loading.visibility = View.GONE

                        //RecyclerViewの設定
                        //setHasFixedSizeはRecyclerViewのサイズが変わる可能性がある場合falseにする
                        binding.recyclerView.setHasFixedSize(true)

                        //Adapterを設定
                        binding.recyclerView.adapter = BookAdapter(data!!)

                        //LayoutManager(アイテムの並べ方を決める)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@GetBookDataActivity)

                        //RecyclerViewに境界線を表示する処理
                        val dividerItemDecoration = DividerItemDecoration(
                            this@GetBookDataActivity,
                            RecyclerView.VERTICAL
                        )
                        binding.recyclerView.addItemDecoration(dividerItemDecoration)
                    }

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

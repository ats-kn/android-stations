package com.example.techtrain.railway.android

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.databinding.ActivityBookrevieweditorBinding
import com.example.techtrain.railway.android.utils.BookReviewValidation
import com.example.techtrain.railway.android.utils.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookReviewEditorActivity : AppCompatActivity(){
    private lateinit var binding: ActivityBookrevieweditorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookrevieweditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // BookReviewActivityから遷移した際に受け取るbookIdを取得
        val bookId = intent.getStringExtra(getString(R.string.bookid))

        // トークンを取得
        val token = getSharedPreferences(
            getString(R.string.preference),
            MODE_PRIVATE,
        ).getString(getString(R.string.token_key), null)

        // textWatcherを設定
        BookReviewValidation.createTextWatcherBookReview(
            this,
            binding.editTextTitle,
            binding.editTextUrl,
            binding.editTextDetail,
            binding.editTextReview,
            null
        )

        // 本の詳細情報を取得
        val getBookDetail = service.getBookDetail("Bearer $token", bookId!!)
        getBookDetail.enqueue(
            object : Callback<Book> {
                override fun onResponse(
                    call: Call<Book>,
                    response: Response<Book>,
                ) {
                    if (response.isSuccessful) {
                        // progressBarを非表示
                        binding.progressBar.visibility = View.GONE
                        val data = response.body()

                        // 本の詳細情報をEditTextに設定
                        // dataがnullの場合、空文字を設定
                        binding.editTextTitle.setText(data?.title ?: "")
                        binding.editTextUrl.setText(data?.url ?: "")
                        binding.editTextDetail.setText(data?.detail ?: "")
                        binding.editTextReview.setText(data?.review ?: "")


                    }else{
                        Log.e("BookDetailActivity", "onResponse: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<Book>, t: Throwable) {
                    Log.e("BookDetailActivity", "onFailure: $t")
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bookrevieweditor, menu)
        return true
    }
}

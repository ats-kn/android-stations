package com.example.techtrain.railway.android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.databinding.ActivityBookdetailBinding
import com.example.techtrain.railway.android.utils.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookdetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // BookReviewActivityから遷移した際に受け取るbookIdを取得
        val bookId = intent.getStringExtra(getString(R.string.bookid))

        // トークンを取得
        val token =
            getSharedPreferences(
                getString(R.string.preference),
                MODE_PRIVATE,
            ).getString(getString(R.string.token_key), null)

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
                        // 本の詳細情報を表示
                        binding.bookTitle.text = data!!.title
                        // URLに下線を追加
                        binding.bookUrl.paint.isUnderlineText = true
                        binding.bookUrl.text = data.url
                        binding.bookDetail.text = data.detail
                        binding.bookReview.text = data.review
                        binding.bookReviewer.text = data.reviewer

                        binding.bookUrl.setOnClickListener {
                            // WebViewActivityに遷移
                            val intent =
                                Intent(this@BookDetailActivity, WebViewActivity::class.java)
                            intent.putExtra("URL", binding.bookUrl.text.toString())
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(
                            this@BookDetailActivity,
                            getString(R.string.fail_get_bookdetail),
                            Toast.LENGTH_SHORT,
                        ).show()
                        finish()
                    }
                }

                override fun onFailure(
                    call: Call<Book>,
                    t: Throwable,
                ) {
                    Toast.makeText(
                        this@BookDetailActivity,
                        getString(R.string.fail_network),
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()
                }
            },
        )
    }
}

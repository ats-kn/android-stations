package com.example.techtrain.railway.android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.databinding.ActivityReviewpostBinding
import com.example.techtrain.railway.android.utils.BookReviewValidation
import com.example.techtrain.railway.android.utils.service
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewpostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Bindingの設定
        binding = ActivityReviewpostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // textWatcherの設定
        val textWatcher = BookReviewValidation.createTextWatcherBookReview(
            this,
            binding.editTextTitle,
            binding.editTextUrl,
            binding.editTextDetail,
            binding.editTextReview,
            binding.postButton
        )

        binding.editTextTitle.addTextChangedListener(textWatcher)
        binding.editTextUrl.addTextChangedListener(textWatcher)
        binding.editTextDetail.addTextChangedListener(textWatcher)
        binding.editTextReview.addTextChangedListener(textWatcher)

        // レビュー投稿ボタンが押された時の処理
        binding.postButton.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val url = binding.editTextUrl.text.toString()
            val detail = binding.editTextDetail.text.toString()
            val review = binding.editTextReview.text.toString()
            val book = Book("",title, url, detail, review, "", null)

            // トークンの取得
            val token =
                getSharedPreferences(
                    getString(R.string.preference),
                    MODE_PRIVATE,
                ).getString(getString(R.string.token_key), null)

            // 非同期通信でレビューを投稿
            val postBookReview = service.postReview("Bearer $token", book)
            postBookReview.enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        if (response.isSuccessful) {
                            // レビューの投稿に成功したらトーストを表示
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.post_review_success),
                                Toast.LENGTH_SHORT,
                            ).show()

                            // Handlerを使用して遅延を入れる, これによって新規で投稿したレビューが表示される
                            Handler(Looper.getMainLooper()).postDelayed({
                                // BookReviewActivityに遷移
                                val intent = Intent(this@ReviewPostActivity, BookReviewActivity::class.java)
                                startActivity(intent)
                            }, 500)  // 500ミリ秒（0.5秒) 後に遷移
                        } else {
                            // レビューの投稿に失敗したらトーストを表示
                            val errorMessageJp =
                                response.errorBody()?.string()?.substringAfter(
                                    "message\":\"",
                                )?.substringBefore("\"")
                            Toast.makeText(
                                applicationContext,
                                errorMessageJp,
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 通信に失敗したらログを表示
                        Log.d("ReviewPost", t.message.toString())
                    }
                }
            )
        }
    }
}

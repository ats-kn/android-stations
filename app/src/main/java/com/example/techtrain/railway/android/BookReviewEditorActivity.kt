package com.example.techtrain.railway.android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.databinding.ActivityBookrevieweditorBinding
import com.example.techtrain.railway.android.utils.BookReviewValidation
import com.example.techtrain.railway.android.utils.service
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookReviewEditorActivity : AppCompatActivity(){
    private lateinit var binding: ActivityBookrevieweditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookrevieweditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // textWatcherを設定
        BookReviewValidation.createTextWatcherBookReview(
            this,
            binding.editTextTitle,
            binding.editTextUrl,
            binding.editTextDetail,
            binding.editTextReview,
            null
        )

        // BookReviewActivityから遷移した際に受け取るbookIdを取得
        val bookId = intent.getStringExtra(getString(R.string.bookid))

        // トークンを取得
        val token = getSharedPreferences(
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

                        // 本の詳細情報をEditTextに設定
                        // dataがnullの場合、空文字を設定
                        binding.editTextTitle.setText(data?.title ?: "")
                        binding.editTextUrl.setText(data?.url ?: "")
                        binding.editTextDetail.setText(data?.detail ?: "")
                        binding.editTextReview.setText(data?.review ?: "")


                    }else{
                        Toast.makeText(this@BookReviewEditorActivity, "本の詳細情報の取得に失敗しました", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<Book>, t: Throwable) {
                    Toast.makeText(this@BookReviewEditorActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bookrevieweditor, menu)
        return true
    }

    // 保存ボタンを押したらレビュー情報を更新
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                // EditTextから情報を取得
                val title = binding.editTextTitle.text.toString()
                val url = binding.editTextUrl.text.toString()
                val detail = binding.editTextDetail.text.toString()
                val review = binding.editTextReview.text.toString()

                // BookReviewActivityから遷移した際に受け取るbookIdを取得
                val bookId = intent.getStringExtra(getString(R.string.bookid))

                // トークンを取得
                val token = getSharedPreferences(
                    getString(R.string.preference),
                    MODE_PRIVATE,
                ).getString(getString(R.string.token_key), null)

                // Bookオブジェクトを作成
                val book = Book("", title, url, detail, review, "",null)
                // レビュー情報を更新
                val updateReview = service.updateReview("Bearer $token", bookId!!, book)
                updateReview.enqueue(
                    object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>,
                        ) {
                            if (response.isSuccessful) {
                                // 更新が成功した場合の処理を記述
                                Toast.makeText(this@BookReviewEditorActivity, "レビューを更新しました", Toast.LENGTH_SHORT).show()

                                // Handlerを使って画面遷移を遅らせる
                                Handler(Looper.getMainLooper()).postDelayed({
                                    // BookReviewActivityに遷移
                                    val intent = Intent(this@BookReviewEditorActivity, BookReviewActivity::class.java)
                                    startActivity(intent)
                                }, 500)
                            } else {
                                // 更新が失敗した場合の処理を記述
                                Toast.makeText(this@BookReviewEditorActivity, "レビューの更新に失敗しました", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // 通信に失敗した場合の処理を記述
                            Toast.makeText(this@BookReviewEditorActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                )
                true
            }

            // deleteボタンを押したらレビュー情報を削除
            R.id.action_delete -> {
                // BookReviewActivityから遷移した際に受け取るbookIdを取得
                val bookId = intent.getStringExtra(getString(R.string.bookid))

                // トークンを取得
                val token = getSharedPreferences(
                    getString(R.string.preference),
                    MODE_PRIVATE,
                ).getString(getString(R.string.token_key), null)

                // レビュー情報を削除
                val deleteReview = service.deleteReview("Bearer $token", bookId!!)
                deleteReview.enqueue(
                    object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>,
                        ) {
                            if (response.isSuccessful) {
                                // 削除が成功した場合の処理を記述
                                Toast.makeText(this@BookReviewEditorActivity, "レビューを削除しました", Toast.LENGTH_SHORT).show()

                                // Handlerを使って画面遷移を遅らせる
                                Handler(Looper.getMainLooper()).postDelayed({
                                    // BookReviewActivityに遷移
                                    val intent = Intent(this@BookReviewEditorActivity, BookReviewActivity::class.java)
                                    startActivity(intent)
                                }, 500)
                            } else {
                                // 削除が失敗した場合の処理を記述
                                Toast.makeText(this@BookReviewEditorActivity, "レビューの削除に失敗しました", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // 通信に失敗した場合の処理を記述
                            Toast.makeText(this@BookReviewEditorActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

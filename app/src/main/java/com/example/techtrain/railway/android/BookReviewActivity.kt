package com.example.techtrain.railway.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.databinding.ActivityBookreviewBinding
import com.example.techtrain.railway.android.utils.BookAdapter
import com.example.techtrain.railway.android.utils.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Bindingの設定
        binding = ActivityBookreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 書籍一覧情報のGETリクエスト
        val token =
            getSharedPreferences(
                getString(R.string.preference),
                MODE_PRIVATE,
            ).getString(getString(R.string.token_key), null)
        val getBookData = service.getBookDataAuth("Bearer $token")

        getBookData.enqueue(
            object : Callback<List<Book>> {
                override fun onResponse(
                    call: Call<List<Book>>,
                    response: Response<List<Book>>,
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()

                        // プログレスバーを非表示
                        binding.progressBar.visibility = View.GONE
                        // RecyclerViewの設定
                        binding.recyclerView.setHasFixedSize(true)
                        binding.recyclerView.adapter = BookAdapter(data!!) { bookId, isMine->
                            // isMineがtrueの場合、BookReviewEditorActivityに遷移
                            if (isMine == true) {
                                val intent = Intent(
                                    this@BookReviewActivity,
                                    BookReviewEditorActivity::class.java,
                                )
                                intent.putExtra(getString(R.string.bookid), bookId)
                                startActivity(intent)
                            } else {
                                // BookReviewViewerActivityに遷移
                                val intent = Intent(
                                    this@BookReviewActivity,
                                    BookDetailActivity::class.java,
                                )
                                intent.putExtra(getString(R.string.bookid), bookId)
                                startActivity(intent)
                            }
                        }

                        // RecyclerViewのレイアウトマネージャーを設定
                        binding.recyclerView.layoutManager =
                            LinearLayoutManager(this@BookReviewActivity)

                        // RecyclerViewに境界線を表示する処理
                        val dividerItemDecoration =
                            DividerItemDecoration(
                                this@BookReviewActivity,
                                RecyclerView.VERTICAL,
                            )
                        binding.recyclerView.addItemDecoration(dividerItemDecoration)
                    } else {
                        // httpステータスコードが200番台以外の場合、ログを表示
                        Log.e(getString(R.string.bookreview), response.message())
                    }
                }

                // 通信に失敗した場合
                override fun onFailure(
                    call: Call<List<Book>>,
                    t: Throwable,
                ) {
                    Log.e(getString(R.string.bookreview), t.message.toString())
                }
            }
        )

        //フローティングアクションボタンの設定
        binding.fab.setOnClickListener {
            // ReviewPostActivityに遷移
            val intent = Intent(this@BookReviewActivity, ReviewPostActivity::class.java)
            startActivity(intent)
        }
    }

    // ツールバーにメニューを表示
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // ツールバーにメニューを表示
        menuInflater.inflate(R.menu.menu_bookreview, menu)
        return true
    }

    // メニューのアイテムが選択された時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // ユーザー情報編集画面に遷移
            R.id.action_UserInfoEdit -> {
                // UserInfoEditorActivityに遷移
                val intent = Intent(this@BookReviewActivity, UserInfoEditorActivity::class.java)
                startActivity(intent)
                true
            }

            // ログアウト処理
            R.id.action_logout -> {
                val dataStore =
                    getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE)
                val editor = dataStore.edit()
                editor.clear()
                editor.apply()

                // MainActivityに遷移する
                val intent = Intent(this@BookReviewActivity, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

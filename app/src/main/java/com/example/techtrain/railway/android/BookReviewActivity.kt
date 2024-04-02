package com.example.techtrain.railway.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.databinding.ActivityBookreviewBinding
import com.example.techtrain.railway.android.room.BookReviewDatabase
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

        // Roomのインスタンスを取得
        val db = BookReviewDatabase.getDatabase(this)

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

                        // Roomにデータを保存
                        Thread {
                            db.bookReviewDao().deleteAll()
                            db.bookReviewDao().insertAll(*data!!.toTypedArray())
                        }.start()

                        runOnUiThread {
                            // プログレスバーを非表示
                            binding.progressBar.visibility = View.GONE

                            // RecyclerViewの設定
                            binding.recyclerView.setHasFixedSize(true)
                            binding.recyclerView.adapter = BookAdapter(data!!) { bookId, isMine ->
                                // isMineがtrueの場合、BookReviewEditorActivityに遷移
                                val intent = if (isMine == true) {
                                    Intent(
                                        this@BookReviewActivity,
                                        BookReviewEditorActivity::class.java
                                    )
                                } else {
                                    // isMineがfalseの場合、BookDetailActivityに遷移
                                    Intent(this@BookReviewActivity, BookDetailActivity::class.java)
                                }
                                intent.putExtra(getString(R.string.bookid), bookId)
                                startActivity(intent)
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
                        }
                    } else {
                        // Roomからデータを取得
                        Thread {
                            val data = db.bookReviewDao().getAll()
                            runOnUiThread {
                                // プログレスバーを非表示
                                binding.progressBar.visibility = View.GONE

                                // RecyclerViewの設定
                                binding.recyclerView.setHasFixedSize(true)
                                binding.recyclerView.adapter = BookAdapter(data) { bookId, isMine->
                                    // isMineがtrueの場合、BookReviewEditorActivityに遷移
                                    val intent = if (isMine == true) {
                                        Intent(this@BookReviewActivity, BookReviewEditorActivity::class.java)
                                    } else {
                                        // isMineがfalseの場合、BookDetailActivityに遷移
                                        Intent(this@BookReviewActivity, BookDetailActivity::class.java)
                                    }
                                    intent.putExtra(getString(R.string.bookid), bookId)
                                    startActivity(intent)
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
                            }
                        }.start()
                    }
                }

                // 通信に失敗した場合
                override fun onFailure(
                    call: Call<List<Book>>,
                    t: Throwable,
                ) {
                    // Roomからデータを取得
                    Thread {
                        val data = db.bookReviewDao().getAll()
                        runOnUiThread {
                            // プログレスバーを非表示
                            binding.progressBar.visibility = View.GONE

                            // RecyclerViewの設定
                            binding.recyclerView.setHasFixedSize(true)
                            binding.recyclerView.adapter = BookAdapter(data) { bookId, isMine->
                                // isMineがtrueの場合、BookReviewEditorActivityに遷移
                                val intent = if (isMine == true) {
                                    Intent(this@BookReviewActivity, BookReviewEditorActivity::class.java)
                                } else {
                                    // isMineがfalseの場合、BookDetailActivityに遷移
                                    Intent(this@BookReviewActivity, BookDetailActivity::class.java)
                                }
                                intent.putExtra(getString(R.string.bookid), bookId)
                                startActivity(intent)
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
                        }
                    }.start()
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

            // 更新処理（レビュー情報をGET）
            R.id.action_update -> {
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
                                    val intent = if (isMine == true) {
                                        Intent(this@BookReviewActivity, BookReviewEditorActivity::class.java)
                                    } else {
                                        // isMineがfalseの場合、BookDetailActivityに遷移
                                        Intent(this@BookReviewActivity, BookDetailActivity::class.java)
                                    }
                                    intent.putExtra(getString(R.string.bookid), bookId)
                                    startActivity(intent)
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

                                Toast.makeText(this@BookReviewActivity, "更新しました", Toast.LENGTH_SHORT).show()
                            } else {
                                // httpステータスコードが200番台以外の場合、ログを表示
                                Toast.makeText(this@BookReviewActivity, response.message(), Toast.LENGTH_SHORT).show()
                            }
                        }

                        // 通信に失敗した場合
                        override fun onFailure(
                            call: Call<List<Book>>,
                            t: Throwable,
                        ) {
                            Toast.makeText(this@BookReviewActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

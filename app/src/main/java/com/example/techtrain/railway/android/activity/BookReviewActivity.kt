package com.example.techtrain.railway.android.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techtrain.railway.android.R
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.databinding.ActivityBookreviewBinding
import com.example.techtrain.railway.android.utils.BookAdapter
import com.example.techtrain.railway.android.viewmodel.BookReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookreviewBinding
    private val bookReviewViewModel: BookReviewViewModel by viewModels()

    // RecyclerViewの設定
    private fun setupRecyclerView(data: List<Book>) {
        // プログレスバーを非表示
        binding.progressBar.visibility = View.GONE

        // RecyclerViewの設定
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter =
            BookAdapter(data) { bookId, isMine ->
                // isMineがtrueの場合、BookReviewEditorActivityに遷移
                val intent =
                    if (isMine == true) {
                        Intent(this, BookReviewEditorActivity::class.java)
                    } else {
                        // isMineがfalseの場合、BookDetailActivityに遷移
                        Intent(this, BookDetailActivity::class.java)
                    }
                intent.putExtra(getString(R.string.bookid), bookId)
                startActivity(intent)
            }

        // RecyclerViewのレイアウトマネージャーを設定
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // RecyclerViewに境界線を表示する処理
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Bindingの設定
        binding = ActivityBookreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // フローティングアクションボタンの設定
        binding.fab.setOnClickListener {
            // ReviewPostActivityに遷移
            val intent = Intent(this@BookReviewActivity, ReviewPostActivity::class.java)
            startActivity(intent)
        }

        // 書籍一覧情報のGETリクエスト
        // ViewModelを通じてデータを取得
        bookReviewViewModel.getBookData()

        // ViewModelのLiveDataを観察
        bookReviewViewModel.bookReviewList.observe(this) { data ->
            // RecyclerViewの設定
            setupRecyclerView(data!!)
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
                // トークンを削除
                bookReviewViewModel.logout()
                // MainActivityに遷移
                val intent = Intent(this@BookReviewActivity, MainActivity::class.java)
                // 遷移先に戻れないように設定
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                true
            }

            // 更新処理（レビュー情報をGET）
            R.id.action_update -> {
                // 書籍一覧情報のGETリクエスト
                bookReviewViewModel.getBookData()

                // ViewModelの結果を観察
                bookReviewViewModel.getBookResult.observe(this) { result ->
                    result.fold(
                        onSuccess = {
                            Toast.makeText(
                                this,
                                getString(R.string.refresh),
                                Toast.LENGTH_SHORT,
                            ).show()
                        },
                        onFailure = {
                            Toast.makeText(
                                this,
                                getString(R.string.fail_network),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    )
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

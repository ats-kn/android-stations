package com.example.techtrain.railway.android.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.R
import com.example.techtrain.railway.android.databinding.ActivityBookdetailBinding
import com.example.techtrain.railway.android.viewmodel.BookDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookdetailBinding
    private val viewModel: BookDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ツールバーに戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // BookReviewActivityから遷移した際に受け取るbookIdを取得
        val bookId = intent.getStringExtra(getString(R.string.bookid))

        // 本の詳細情報を取得
        viewModel.getBookDetail(
            bookId!!,
            onFailure = {
                Toast.makeText(
                    this@BookDetailActivity,
                    getString(R.string.fail_get_bookdetail),
                    Toast.LENGTH_SHORT,
                ).show()
                finish()
            },
            onNetworkError = {
                Toast.makeText(
                    this@BookDetailActivity,
                    getString(R.string.fail_network),
                    Toast.LENGTH_SHORT,
                ).show()
                finish()
            },
        )

        viewModel.bookDetail.observe(this) { book ->
            // 本の詳細情報を表示
            book?.let {
                // プログレスバーを非表示
                binding.progressBar.visibility = View.GONE

                binding.bookTitle.text = it.title
                binding.bookUrl.paint.isUnderlineText = true
                binding.bookUrl.text = it.url
                binding.bookDetail.text = it.detail
                binding.bookReview.text = it.review
                binding.bookReviewer.text = it.reviewer
                // URLがクリックされたときの処理
                binding.bookUrl.setOnClickListener {
                    val intent = Intent(this@BookDetailActivity, WebViewActivity::class.java)
                    intent.putExtra("URL", binding.bookUrl.text.toString())
                    startActivity(intent)
                }
            }
        }
    }

    // ツールバーのボタンが押されたときの処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // 戻るボタンが押されたときの処理
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

package com.example.techtrain.railway.android.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.databinding.BookItemBinding

// List<Book>を受け取るBookAdapterクラスを作成
class BookAdapter(
    private val books: List<Book>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    // ViewHolderの設定(複数回使用するレイアウトを保存)
    class BookViewHolder(binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.title
        val detail: TextView = binding.detail
        val review: TextView = binding.review
        val reviewer: TextView = binding.reviewer
    }

    // ViewHolderにレイアウトを格納して返す役割
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookItemBinding.inflate(inflater, parent, false)
        return BookViewHolder(binding)
    }

    // ViewHolderにデータをバインド(Bookデータを描画する際に固有の値を当てはめる)
    override fun onBindViewHolder(
        holder: BookViewHolder,
        position: Int,
    ) {
        val book = books[position]
        holder.title.text = book.title
        holder.detail.text = book.detail
        holder.review.text = book.review
        holder.reviewer.text = book.reviewer

        // クリック時の処理
        holder.itemView.setOnClickListener {
            // クリックされたアイテムの情報を取得
            onItemClicked(book.id)
        }
    }

    // データの数を返す
    override fun getItemCount() = books.size
}

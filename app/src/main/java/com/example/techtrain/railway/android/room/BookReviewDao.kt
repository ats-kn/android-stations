package com.example.techtrain.railway.android.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.techtrain.railway.android.data.Book

@Dao
interface BookReviewDao {
    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE id = :bookId")
    fun getBookById(bookId: String): Book

    @Insert
    fun insertAll(vararg books: Book)

    @Insert
    fun insertBook(book: Book)

    @Query("DELETE FROM book")
    fun deleteAll()
}

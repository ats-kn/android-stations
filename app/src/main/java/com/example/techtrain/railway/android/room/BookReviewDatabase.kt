package com.example.techtrain.railway.android.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.techtrain.railway.android.data.Book

@Database(
    entities = [Book::class], version = 2, exportSchema = false
)
abstract class BookReviewDatabase : RoomDatabase() {
    abstract fun bookReviewDao(): BookReviewDao

    companion object {
        @Volatile
        private var INSTANCE: BookReviewDatabase? = null

        fun getDatabase(context: android.content.Context): BookReviewDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    BookReviewDatabase::class.java,
                    "book_review"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}

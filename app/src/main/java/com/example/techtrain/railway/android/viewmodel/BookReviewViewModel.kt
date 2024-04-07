package com.example.techtrain.railway.android.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.techtrain.railway.android.R
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.room.BookReviewDatabase
import com.example.techtrain.railway.android.utils.service
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BookReviewViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel(){
    val bookReviewList = MutableLiveData<List<Book>?>()
    val getBookResult = MutableLiveData<Result<String>>()
    val db = BookReviewDatabase.getDatabase(context)
    private val sharedPref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.preference), Context.MODE_PRIVATE)
    private val token = sharedPref.getString(context.getString(R.string.token_key), "")

    fun getBookData() {
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

                        // LiveDataを更新
                        bookReviewList.postValue(data)

                        // 通信に成功した場合
                        getBookResult.postValue(Result.success("Success"))
                    } else {
                        // Roomからデータを取得
                        Thread {
                            val data = db.bookReviewDao().getAll()
                            // LiveDataを更新
                            bookReviewList.postValue(data)
                        }.start()
                    }
                }

                // 通信に失敗した場合
                override fun onFailure(
                    call: Call<List<Book>>,
                    t: Throwable,
                ) {
                }
            }
        )
    }

    private val logoutResult = MutableLiveData<Boolean>()
    fun logout() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
        logoutResult.postValue(true)
    }

}

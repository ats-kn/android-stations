package com.example.techtrain.railway.android.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.techtrain.railway.android.R
import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.utils.service
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel(){
    val bookDetail = MutableLiveData<Book?>()
    private val sharedPref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.preference), Context.MODE_PRIVATE)
    private val token = sharedPref.getString(context.getString(R.string.token_key), "")

    fun getBookDetail(
        bookId: String,
        onFailure: () -> Unit,
        onNetworkError: () -> Unit,
        ) {
        val getBookDetailCall = service.getBookDetail("Bearer $token", bookId)
        getBookDetailCall.enqueue(
            object : Callback<Book> {
                override fun onResponse(
                    call: Call<Book>,
                    response: Response<Book>,
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        bookDetail.postValue(data)
                    } else {
                        // Handle error
                        onFailure()
                    }
                }

                override fun onFailure(
                    call: Call<Book>,
                    t: Throwable,
                ) {
                    // Handle failure
                    onNetworkError()
                }
            }
        )
    }
}



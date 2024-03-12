package com.example.techtrain.railway.android.utils

import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.data.Login
import com.example.techtrain.railway.android.data.User
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL =
    "https://railway.bookreview.techtrain.dev"

//Moshiのインスタンスを作成
private val moshi = Moshi.Builder()
    .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
    .build()

//Retrofitのインスタンスを作成
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL) //ベースURLを指定
    .addConverterFactory(MoshiConverterFactory.create(moshi)) //Moshiを使用してJSONをパース
    .build()

//APIインターフェースの実体を作成
val service: BookApiService = retrofit.create(BookApiService::class.java)

//BookApiServiceインターフェイス(インターフェイス：複数のクラスで使用できる共通メソッド)を作成
interface BookApiService {
    // Book情報を取得
    @GET("public/books") //pathを指定
    fun getBookData(): Call<List<Book>>//受け取るデータクラスを指定

    // ユーザー情報を登録
    @POST("users")
    fun signin(@Body user: User): Call<ResponseBody>//サーバからのレスポンスはResponseBodyとして受け取る

    // ユーザー認証
    @POST("signin")
    fun login(@Body login: User): Call<ResponseBody>//サーバからのレスポンスはResponseBodyとして受け取る
}

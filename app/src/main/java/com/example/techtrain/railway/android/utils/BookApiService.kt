package com.example.techtrain.railway.android.utils

import com.example.techtrain.railway.android.data.Book
import com.example.techtrain.railway.android.data.User
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

private const val BASE_URL =
    "https://railway.bookreview.techtrain.dev"

//Moshiのインスタンスを作成
private val moshi = Moshi.Builder()
    .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
    .build()

//Retrofitのインスタンスを作成
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

//APIインターフェースの実体を作成
val service: BookApiService = retrofit.create(BookApiService::class.java)

//BookApiServiceインターフェイス(インターフェイス：複数のクラスで使用できる共通メソッド)を作成
interface BookApiService {
    // Book情報を取得
    @GET("public/books") //pathを指定
    fun getBookData(): Call<List<Book>>//受け取るデータクラスを指定

    @GET("books")
    fun getBookDataAuth(@Header("Authorization") token: String): Call<List<Book>>

    // ユーザー情報を登録
    @POST("users")
    fun signin(@Body user: User): Call<ResponseBody>//サーバからのレスポンスはResponseBodyとして受け取る

    // ユーザー認証
    @POST("signin")
    fun login(@Body login: User): Call<ResponseBody>

    // ユーザー情報更新
    @PUT("users")
    fun updateUserInfo(@Header("Authorization") token: String, @Body user: User): Call<ResponseBody>
}

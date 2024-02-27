package com.example.techtrain.railway.android

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://railway.bookreview.techtrain.dev"

//Moshi Builderを作成
private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

//Retrofit Builderを作成
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL) //ベースURLを指定
    .addConverterFactory(MoshiConverterFactory.create(moshi)) //Moshiを使用してJSONをパース
    .build()

//APIのServiceを作成
val service: BookApiService = retrofit.create(BookApiService::class.java)

//BookApiServiceインターフェイス(インターフェイス：複数のクラスで使用できる共通メソッド)を作成
interface BookApiService {
    @GET("public/books") // エンドポイントのURLを指定します
    fun getBookData(): Call<List<Book>>// 受け取るデータクラスを指定します
}


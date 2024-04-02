package com.example.techtrain.railway.android.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey val id: String,
    val title: String,
    val url: String,
    val detail: String,
    val review: String,
    val reviewer: String,
    val isMine: Boolean?
)

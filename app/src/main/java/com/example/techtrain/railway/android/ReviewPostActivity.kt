package com.example.techtrain.railway.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityReviewpostBinding

class ReviewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewpostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewpostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

package com.example.techtrain.railway.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityBookreviewBinding

class BookReviewActivity: AppCompatActivity(){
    private lateinit var binding: ActivityBookreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Bindingの設定
        binding = ActivityBookreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}

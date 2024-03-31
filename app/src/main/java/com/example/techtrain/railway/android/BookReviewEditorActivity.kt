package com.example.techtrain.railway.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityBookrevieweditorBinding

class BookReviewEditorActivity : AppCompatActivity(){
    private lateinit var binding: ActivityBookrevieweditorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookrevieweditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

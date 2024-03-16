package com.example.techtrain.railway.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityUserinfoeditorBinding

class UserInfoEditorActivity: AppCompatActivity() {
    // ViewBindingを使うための変数を定義
    private lateinit var binding: ActivityUserinfoeditorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bindingの設定
        binding = ActivityUserinfoeditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

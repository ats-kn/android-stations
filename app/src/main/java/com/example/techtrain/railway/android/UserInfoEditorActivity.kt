package com.example.techtrain.railway.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityUserinfoeditorBinding
import com.example.techtrain.railway.android.utils.ValidationUtils

class UserInfoEditorActivity: AppCompatActivity() {
    // ViewBindingを使うための変数を定義
    private lateinit var binding: ActivityUserinfoeditorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserinfoeditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editTextNameEdit

        val textWatcher = ValidationUtils.createTextWatcher(
            this,
            binding.editTextNameEdit,
            null,
            null,
            binding.UserInfoEditButton,
            isValidInput = { name, _, _ ->
                // nameのバリデーション確認
                ValidationUtils.isValidName(name)
            }
        )

        binding.editTextNameEdit.addTextChangedListener(textWatcher)

        binding.UserInfoEditButton.setOnClickListener {
            // ユーザー名を取得
        }

    }
}

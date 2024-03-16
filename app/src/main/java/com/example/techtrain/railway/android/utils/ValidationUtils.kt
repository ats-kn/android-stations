package com.example.techtrain.railway.android.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.techtrain.railway.android.R

object ValidationUtils {

    // 入力された値が正しいかどうかを判定しボタンを制御するTextWatcherを作成
    fun createTextWatcher(
        activity: AppCompatActivity,
        nameEditText: EditText?,
        emailEditText: EditText,
        passwordEditText: EditText,
        button: Button
    ): TextWatcher {

        // ボタンを無効化
        button.isEnabled = false
        button.backgroundTintList = ContextCompat.getColorStateList(activity, R.color.disabled_button_color)

        // ユーザー名のバリデーション
        // 3文字以上のユーザー名を許可
        fun isValidName(name: String): Boolean {
            return name.length >= 3
        }

        // メールアドレスのバリデーション
        fun isValidEmail(email: String): Boolean {
            val emailRegex = activity.getString(R.string.validation_email).toRegex()
            return emailRegex.matches(email)
        }

        // パスワードのバリデーション
        fun isValidPassword(password: String): Boolean {
            val passwordRegex = activity.getString(R.string.validation_password).toRegex()
            return passwordRegex.matches(password)
        }

        // キーボードを隠すためのInputMethodManagerを取得
        val inputMethodManager = getSystemService(activity, InputMethodManager::class.java)

        // フォーカスが外れたときにバリデーションを行う(name)
        nameEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // nameEditText以外をタップでキーボードを隠す
                inputMethodManager?.hideSoftInputFromWindow(
                    nameEditText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )

                val name = nameEditText.text.toString()
                if (!isValidName(name)) {
                    // nameの要件を満たしていないことを表示
                    nameEditText.error = activity.getString(R.string.check_name)
                }
            }
        }

        // フォーカスが外れたときにバリデーションを行う(email)
        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // emailEditText以外をタップでキーボードを隠す
                inputMethodManager?.hideSoftInputFromWindow(
                    emailEditText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )

                val email = emailEditText.text.toString()
                if (!isValidEmail(email)) {
                    // emailの要件を満たしていないことを表示
                    emailEditText.error = activity.getString(R.string.check_email)
                }
            }
        }

        // フォーカスが外れたときにバリデーションを行う(password)
        passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // passwordEditText以外をタップでキーボードを隠す
                inputMethodManager?.hideSoftInputFromWindow(
                    passwordEditText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )

                val password = passwordEditText.text.toString()
                if (!isValidPassword(password)) {
                    // passwordの要件を満たしていないことを表示
                    passwordEditText.error = activity.getString(R.string.check_password)
                }
            }
        }

        // リアルタイムに値を取得できるTextWatcherを使用
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val name = nameEditText?.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                // メールアドレスとパスワードが正しい場合、ボタンを有効化
                if (isValidName(name) && isValidEmail(email) && isValidPassword(password)) {
                    button.isEnabled = true
                    button.backgroundTintList = ContextCompat.getColorStateList(activity, R.color.enabled_button_color)
                } else {
                    button.isEnabled = false
                    button.backgroundTintList = ContextCompat.getColorStateList(activity, R.color.disabled_button_color)
                }
            }

            // 以降のメソッドは使わないけど消せないので空実装
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }
}

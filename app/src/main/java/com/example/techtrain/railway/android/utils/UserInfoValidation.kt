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

object UserInfoValidation {
    // 入力された値が正しいかどうかを判定しボタンを制御するTextWatcherを作成
    fun createTextWatcherUserInfo(
        activity: AppCompatActivity,
        nameEditText: EditText?,
        emailEditText: EditText?,
        passwordEditText: EditText?,
        button: Button,
        isValidInput: (String, String, String) -> Boolean,
    ): TextWatcher {
        // ボタンを無効化
        button.isEnabled = false
        button.backgroundTintList =
            ContextCompat.getColorStateList(
                activity,
                R.color.disabled_button_color,
            )

        // キーボードを隠すためのInputMethodManagerを取得
        val inputMethodManager = getSystemService(activity, InputMethodManager::class.java)

        // フォーカスが外れたときにバリデーションを行う(name)
        setFocusChangeListener(activity, nameEditText, inputMethodManager, ::isValidName, R.string.check_name)

        // フォーカスが外れたときにバリデーションを行う(email)
        setFocusChangeListener(activity, emailEditText, inputMethodManager, ::isValidEmail, R.string.check_email)

        // フォーカスが外れたときにバリデーションを行う(password)
        setFocusChangeListener(activity, passwordEditText, inputMethodManager, ::isValidPassword, R.string.check_password)

        // リアルタイムに値を取得できるTextWatcherを使用
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val name = nameEditText?.text.toString()
                val email = emailEditText?.text.toString()
                val password = passwordEditText?.text.toString()

                // すべてのEditTextの値が正しい場合、ボタンを有効化
                if (isValidInput(name, email, password)) {
                    button.isEnabled = true
                    button.backgroundTintList =
                        ContextCompat.getColorStateList(
                            activity,
                            R.color.enabled_button_color,
                        )
                } else {
                    button.isEnabled = false
                    button.backgroundTintList =
                        ContextCompat.getColorStateList(
                            activity,
                            R.color.disabled_button_color,
                        )
                }
            }

            // 以降のメソッドは使わないけど消せないので空実装
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int,
            ) {}
        }
    }

    // フォーカスが外れたときにバリデーションを行う
    private fun setFocusChangeListener(
        activity: AppCompatActivity,
        editText: EditText?,
        inputMethodManager: InputMethodManager?,
        isValid: (String) -> Boolean,
        errorMessageResId: Int
    ) {
        editText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // editText以外をタップでキーボードを隠す
                inputMethodManager?.hideSoftInputFromWindow(
                    editText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS,
                )

                val text = editText.text.toString()
                if (!isValid(text)) {
                    // textの要件を満たしていないことを表示
                    editText.error = activity.getString(errorMessageResId)
                }
            }
        }
    }

    // ユーザー名のバリデーション
    // 3文字以上のユーザー名を許可
    fun isValidName(name: String): Boolean {
        val nameRegex = Regex("^.{3,}$")
        return nameRegex.matches(name)
    }

    // メールアドレスのバリデーション
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+")
        return emailRegex.matches(email)
    }

    // パスワードのバリデーション
    // 大文字、小文字、数字を含む8文字以上のパスワードを許可
    fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}\$")
        return passwordRegex.matches(password)
    }
}

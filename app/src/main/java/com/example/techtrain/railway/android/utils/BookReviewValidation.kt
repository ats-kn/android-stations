package com.example.techtrain.railway.android.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.techtrain.railway.android.R

object BookReviewValidation {
    // 入力された値が正しいかどうかを判定しボタンを制御するTextWatcherを作成
    fun createTextWatcherBookReview(
        activity: AppCompatActivity,
        titleEditText: EditText?,
        urlEditText: EditText?,
        detailEditText: EditText?,
        reviewEditText: EditText?,
        button: Button
    ): TextWatcher {
        // ボタンを無効化
        button.isEnabled = false
        button.backgroundTintList =
            ContextCompat.getColorStateList(
                activity,
                R.color.disabled_button_color,
            )

        // キーボードを隠すためのInputMethodManagerを取得
        val inputMethodManager =
            ContextCompat.getSystemService(activity, InputMethodManager::class.java)

        // フォーカスが外れたときにバリデーションを行う(title)
        titleEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // titleEditText以外をタップでキーボードを隠す
                inputMethodManager?.hideSoftInputFromWindow(
                    titleEditText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS,
                )

                val title = titleEditText.text.toString()
                if (!isValidTitle(title)) {
                    // titleの要件を満たしていないことを表示
                    titleEditText.error = activity.getString(R.string.check_title)
                }
            }
        }

        // フォーカスが外れたときにバリデーションを行う(url)
        urlEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // urlEditText以外をタップでキーボードを隠す
                inputMethodManager?.hideSoftInputFromWindow(
                    urlEditText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS,
                )

                val url = urlEditText.text.toString()
                if (!isValidUrl(url)) {
                    // urlの要件を満たしていないことを表示
                    urlEditText.error = activity.getString (R.string.check_url)
                }
            }
        }

        // フォーカスが外れたときにバリデーションを行う(detail)
        detailEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // detailEditText以外をタップでキーボードを隠す
                inputMethodManager?.hideSoftInputFromWindow(
                    detailEditText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS,
                )

                val detail = detailEditText.text.toString()
                if (!isValidDetail(detail)) {
                    // detailの要件を満たしていないことを表示
                    detailEditText.error = activity.getString(R.string.check_detail)
                }
            }
        }

        // フォーカスが外れたときにバリデーションを行う(review)
        reviewEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // reviewEditText以外をタップでキーボードを隠す
                inputMethodManager?.hideSoftInputFromWindow(
                    reviewEditText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS,
                )

                val review = reviewEditText.text.toString()
                if (!isValidReview(review)) {
                    // reviewの要件を満たしていないことを表示
                    reviewEditText.error = activity.getString(R.string.check_review)
                }
            }
        }

        // リアルタイムに値を取得できるTextWatcherを使用
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val title = titleEditText?.text.toString()
                val url = urlEditText?.text.toString()
                val detail = detailEditText?.text.toString()
                val review = reviewEditText?.text.toString()

                // すべてのEditTextの値が正しい場合、ボタンを有効化
                if ( isValidTitle(title) && isValidUrl(url) && isValidDetail(detail) && isValidReview(review)){
                    button.isEnabled = true
                    button.backgroundTintList =
                        ContextCompat.getColorStateList(
                            activity,
                            R.color.enabled_button_color
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

    // タイトルのバリデーション
    // 1文字以上のタイトル名を許可
    private fun isValidTitle(title: String): Boolean {
        return title.isNotEmpty()
    }


    // urlのバリデーション
    // https:// または http:// で始まるURLを許可
    private fun isValidUrl(url: String): Boolean {
        val urlRegex = Regex("^(https?://).*$")
        return urlRegex.matches(url)
    }

    // detailのバリデーション
    // 1文字以上の詳細を許可
    private fun isValidDetail(detail: String): Boolean {
        return detail.isNotEmpty()
    }

    // reviewのバリデーション
    // 1文字以上のレビューを許可
    private fun isValidReview(review: String): Boolean {
        return review.isNotEmpty()
    }
}

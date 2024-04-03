package com.example.techtrain.railway.android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.data.User
import com.example.techtrain.railway.android.databinding.ActivityUserinfoeditorBinding
import com.example.techtrain.railway.android.utils.UserInfoValidation
import com.example.techtrain.railway.android.utils.service
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoEditorActivity : AppCompatActivity() {
    // ViewBindingを使うための変数を定義
    private lateinit var binding: ActivityUserinfoeditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserinfoeditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editTextUpdateName

        // ツールバーに戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val textWatcher =
            UserInfoValidation.createTextWatcherUserInfo(
                this,
                binding.editTextUpdateName,
                null,
                null,
                binding.UserInfoEditButton,
                isValidInput = { name, _, _ ->
                    // nameのバリデーション確認
                    UserInfoValidation.isValidName(name)
                },
            )

        binding.editTextUpdateName.addTextChangedListener(textWatcher)

        binding.UserInfoEditButton.setOnClickListener {
            // トークンを取得
            val token =
                getSharedPreferences(
                    getString(R.string.preference),
                    MODE_PRIVATE,
                ).getString(getString(R.string.token_key), null)
            val name = binding.editTextUpdateName.text.toString()
            val user = User(name, "", "")
            val updateUserInfo = service.updateUserInfo("Bearer $token", user)

            updateUserInfo.enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        if (response.isSuccessful) {
                            // ユーザー情報の更新に成功したらトーストを表示
                            Toast.makeText(
                                applicationContext,
                                name + getString(R.string.edit_user_info_success),
                                Toast.LENGTH_SHORT,
                            ).show()

                            // Handlerを使って画面遷移を遅らせる
                            Handler(Looper.getMainLooper()).postDelayed({
                                // BookReviewActivityに遷移
                                val intent =
                                    Intent(
                                        this@UserInfoEditorActivity,
                                        BookReviewActivity::class.java,
                                    )
                                startActivity(intent)
                            }, 500)
                        } else {
                            // ユーザー情報の更新に失敗したらエラーメッセージをトーストで表示
                            val errorMessageJp =
                                response.errorBody()?.string()?.substringAfter(
                                    "message\":\"",
                                )?.substringBefore("\"")
                            Toast.makeText(
                                applicationContext,
                                errorMessageJp,
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseBody>,
                        t: Throwable,
                    ) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.fail_network),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                },
            )
        }
    }

    // ツールバーの戻るボタンが押された時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // 戻るボタンが押されたときの処理
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

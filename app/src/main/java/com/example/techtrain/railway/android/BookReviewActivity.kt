package com.example.techtrain.railway.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.techtrain.railway.android.databinding.ActivityBookreviewBinding

class BookReviewActivity: AppCompatActivity(){
    private lateinit var binding: ActivityBookreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Bindingの設定
        binding = ActivityBookreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // ログアウトボタンを表示する
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // ログアウトボタンが押された時の処理
        return when (item.itemId) {
            // トークンを削除
            R.id.action_logout -> {
                val dataStore = getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE)
                val editor = dataStore.edit()
                editor.clear()
                editor.apply()

                // MainActivityに遷移する
                val intent = Intent(this@BookReviewActivity, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }
}

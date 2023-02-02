package com.gorkemozcan.yorumluyorum

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.gorkemozcan.yorumluyorum.view.activities.MainActivity

class Splash: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 500)
    }
}
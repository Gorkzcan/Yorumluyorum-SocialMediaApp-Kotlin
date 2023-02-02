package com.gorkemozcan.yorumluyorum.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.gorkemozcan.yorumluyorum.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()


        if(auth.currentUser != null){
            binding.btnLogin2.visibility = View.GONE
            binding.btnSignup2.visibility = View.GONE
            binding.pbMainActivity.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed(Runnable{
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }, 500)
        }
        binding.btnLogin2.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnSignup2.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
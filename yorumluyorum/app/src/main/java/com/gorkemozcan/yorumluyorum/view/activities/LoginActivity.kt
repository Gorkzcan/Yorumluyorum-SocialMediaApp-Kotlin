package com.gorkemozcan.yorumluyorum.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.gorkemozcan.yorumluyorum.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        val view = binding.root
        setContentView(view)

        binding.btLogIn.setOnClickListener {
            logInUser(binding.textInputEtEmail.text.toString(), binding.textInputEtPassword.text.toString())

        }

    }

    private fun logInUser(email:String, password: String) = CoroutineScope(Dispatchers.IO).launch {
        if(email.isNotEmpty() && password.isNotEmpty()){
            try{
                auth.signInWithEmailAndPassword(email,password).await()
                withContext(Dispatchers.Main){
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finishAffinity()
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
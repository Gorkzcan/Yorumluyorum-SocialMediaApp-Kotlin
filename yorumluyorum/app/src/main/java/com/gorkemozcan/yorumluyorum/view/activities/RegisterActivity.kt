package com.gorkemozcan.yorumluyorum.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.gorkemozcan.yorumluyorum.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.btSignUp.setOnClickListener {
            registerUser()
        }
    }


    private fun registerUser() = CoroutineScope(Dispatchers.IO).launch {
        val fullName = binding.TextInputEtFullName.text.toString()
        val email = binding.textInputEtEmail.text.toString()
        val password = binding.textInputEtPassword.text.toString()
        val confirmPassword = binding.TextInputConfirmPassword.text.toString()
        val userName = binding.TextInputEtUserName.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
            if(password != confirmPassword){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@RegisterActivity, "Şifreniz ve Şifre onaylama kutucukları eşleşmiyor", Toast.LENGTH_SHORT).show()
                }
            }else if(fullName.isEmpty() && userName.isEmpty()){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@RegisterActivity, "Lütfen Adınızı ve kullanıcı adınızı doldurun", Toast.LENGTH_SHORT).show()
                }
            }else{
                try{
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main){
                        startActivity(Intent(this@RegisterActivity, UserDetailActivity::class.java))
                        finishAffinity()
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
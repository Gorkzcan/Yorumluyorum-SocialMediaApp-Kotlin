package com.gorkemozcan.yorumluyorum.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gorkemozcan.yorumluyorum.R
import com.gorkemozcan.yorumluyorum.databinding.ActivityHomeBinding
import com.gorkemozcan.yorumluyorum.view.fragments.ChatFragment
import com.gorkemozcan.yorumluyorum.view.fragments.FeedFragment
import com.gorkemozcan.yorumluyorum.view.fragments.ProfileFragment
import com.gorkemozcan.yorumluyorum.viewmodel.HomeActivityViewModel
import com.gorkemozcan.yorumluyorum.viewmodel.PostViewModel

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()

        binding.bottomNavView.background = null
        binding.bottomNavView.menu.getItem(2).isEnabled = false

        val viewModel: HomeActivityViewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)
        viewModel.fragment().observe(this, Observer {
            setCurrentFragment(it)
        })
        val viewModel2 : PostViewModel = ViewModelProvider(this)[PostViewModel::class.java]
        val bundle: Bundle? = intent.extras
//        setCurrentFragment(feedFragment)
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bnHome -> viewModel.setCurrFragment(FeedFragment())
                R.id.bnProfile -> {
                    Log.d("Profile", "Clicked")
                    viewModel.setCurrFragment(ProfileFragment())
                }
                R.id.bnChat -> {
                    viewModel.setCurrFragment(ChatFragment())
                }
                R.id.bnExit -> {
                        auth.signOut()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()


                }

            }
            true
        }

        binding.FloatingActionButton.setOnClickListener{
            startActivity(Intent(this@HomeActivity, AddPostActivity::class.java))
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.Fragment_Base, fragment)
            commit()
        }
    }
}
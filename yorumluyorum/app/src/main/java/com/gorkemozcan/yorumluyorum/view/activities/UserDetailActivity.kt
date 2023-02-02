package com.gorkemozcan.yorumluyorum.view.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gorkemozcan.yorumluyorum.databinding.ActivityUserDetailBinding
import com.gorkemozcan.yorumluyorum.model.UserModel
import com.gorkemozcan.yorumluyorum.viewmodel.FirebaseViewModel

class UserDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserDetailBinding
    lateinit var auth : FirebaseAuth
    lateinit var viewModel : FirebaseViewModel
    lateinit var imageUri : Uri
    var storageReference = Firebase.storage.reference
    var User = UserModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        viewModel.user().observe(this , Observer {
            if(it.bio == null){
                it.bio = ""
            }
            if (it.fullname == null){
                it.fullname = ""
            }
            if (it.userName == null){
                it.userName = ""
            }

            binding.TextInputEtBio.setText(it.bio.toString())
            binding.TextInputEtName.setText(it.bio.toString())
            binding.TextInputEtUserName.setText(it.bio.toString())
            Glide.with(this).load(it.photoURL).into(binding.ProfilePic)
            User.photoURL = it.photoURL
        })

        binding.ProfilePic.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Profil Fotoğrafınızı Güncellemek mi İstiyorsunuz?")
            builder.setMessage("Galeri İznine ihtiyacım var.")
            builder.setPositiveButton("Evet"){dialogInterface, which ->
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, 100)
            }
            builder.setNegativeButton("Hayır"){dialogInterface, which ->
                Toast.makeText(this@UserDetailActivity, "İşlem İptal Edildi", Toast.LENGTH_SHORT).show()
            }
            val alertDialog : AlertDialog = builder.create()
            binding.btDone.visibility = View.GONE
            binding.progressBarImageLoading.visibility = View.VISIBLE
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
        binding.btDone.setOnClickListener {
            User.uid = auth.currentUser?.uid
            User.fullname = binding.TextInputEtName.text.toString()
            User.bio = binding.TextInputEtBio.text.toString()
            User.userName = binding.TextInputEtUserName.text.toString()
            User.following = viewModel.user().value?.following
            User.followers = viewModel.user().value?.followers

            Log.d("@@onDoneClick", "${User}")
            viewModel.saveUser(User)
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            imageUri = data?.data!!
            Glide.with(this).load(imageUri).into(binding.ProfilePic)

            try{
                storageReference.child("${auth.currentUser?.uid}/profilePic").putFile(imageUri)
                    .addOnSuccessListener {
                        storageReference
                            .child("${auth.currentUser?.uid}/profilePic")
                            .downloadUrl.addOnSuccessListener {
                                User.photoURL = it.toString()
                                binding.progressBarImageLoading.visibility = View.GONE
                                binding.btDone.visibility = View.VISIBLE

                            }.addOnFailureListener {
                                Log.d("@@@@", it.message.toString())
                                Toast.makeText(this@UserDetailActivity, "Bir hata meydana geldi Tekrar deneyiniz", Toast.LENGTH_SHORT).show()
                            }
                    }.addOnFailureListener {
                        Log.d("@@@@", it.message.toString())
                        Toast.makeText(this@UserDetailActivity, "Bir hata meydana geldi Tekrar deneyiniz", Toast.LENGTH_SHORT).show()
                    }
            }catch (e:Exception){
                Log.d("@@@@", e.message.toString())
                Toast.makeText(this@UserDetailActivity, "Bir hata meydana geldi Tekrar deneyiniz", Toast.LENGTH_SHORT).show()
            }
            Log.d("@@Download and assigned", "${User.photoURL}")
        }
    }
}
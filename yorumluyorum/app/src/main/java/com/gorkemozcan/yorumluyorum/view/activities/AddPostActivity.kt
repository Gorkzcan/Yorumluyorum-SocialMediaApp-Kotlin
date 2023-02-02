package com.gorkemozcan.yorumluyorum.view.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.gorkemozcan.yorumluyorum.databinding.ActivityAddPostBinding
import com.gorkemozcan.yorumluyorum.model.PostModel
import com.gorkemozcan.yorumluyorum.model.UserModel

import com.gorkemozcan.yorumluyorum.viewmodel.FirebaseViewModel
import java.util.*

class AddPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    lateinit var viewModel : FirebaseViewModel
    var db = Firebase.firestore.collection("Posts")
    var User = UserModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]


        registerLauncher()

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

    }


    fun upload(view: View){
        //universal unique id -> override etmeyi önleyecek rastgele bir kod oluşturacak
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)
        if (selectedPicture != null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                //image url -> firestore
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    if (auth.currentUser != null){
                        val post = PostModel(
                            auth.currentUser?.uid,
                            it.toString(),
                            binding.etText.text.toString(),
                            User.fullname,
                            User.userName,
                            User.photoURL,
                            System.currentTimeMillis()
                        )
                        db.add(post).addOnSuccessListener {
                            finish()
                        }.addOnFailureListener {
                            binding.pbAddPost.visibility = View.GONE
                            binding.btDone.visibility = View.VISIBLE
                            Toast.makeText(this@AddPostActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }.addOnFailureListener {
                    binding.pbAddPost.visibility = View.GONE
                    binding.btDone.visibility = View.VISIBLE
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                binding.pbAddPost.visibility = View.GONE
                binding.btDone.visibility = View.VISIBLE
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun selectImage(view:View){
        //Manifeste izin eklendi
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view, "Galeriye gitmek için izin gerekli", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver"){
                    //request permission
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                //request permission
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
            //start activity for result
        }
    }

    private fun registerLauncher() {
       activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
           if (result.resultCode == RESULT_OK){
               val intentFromResult = result.data
               if (intentFromResult != null){
                   selectedPicture = intentFromResult.data
                   selectedPicture?.let {
                       binding.ivImage.setImageURI(it)
                   }
               }
           }
       }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if (result){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                //permission denied
                Toast.makeText(this@AddPostActivity, "İzin gerekli", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
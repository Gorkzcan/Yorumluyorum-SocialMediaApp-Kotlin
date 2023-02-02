package com.gorkemozcan.yorumluyorum.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.bind
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gorkemozcan.yorumluyorum.adapter.ProfilePostAdapter
import com.gorkemozcan.yorumluyorum.databinding.ActivityUserDetailBinding
import com.gorkemozcan.yorumluyorum.databinding.ActivityUsersProfileBinding
import com.gorkemozcan.yorumluyorum.model.PostModel
import com.gorkemozcan.yorumluyorum.model.UserModel
import com.gorkemozcan.yorumluyorum.viewmodel.FirebaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UsersProfile : AppCompatActivity() {

    lateinit var binding : ActivityUsersProfileBinding
    lateinit var user : UserModel
    lateinit var viewModel: FirebaseViewModel
    lateinit var auth: FirebaseAuth
    var db = Firebase.firestore.collection("Posts")
    var db_User = Firebase.firestore.collection("User")
    private var itemList = ArrayList<PostModel>()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ProfilePostAdapter
    var iFollow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]
        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                user = getParcelable("User")!!
                binding.tvName.text = user.fullname
                binding.tvUsername.text = user.userName
                binding.tvBio.text = user.bio
                Glide.with(applicationContext).load(user.photoURL).into(binding.ProfilePic)
                Log.d("@@User to see", "${user}")
            }
        }
        var userTemp = UserModel()
        viewModel.getCurrentUser()
        viewModel.user().observe(this) {
            userTemp = UserModel(
                it.uid, it.fullname,it.userName, it.bio, it.photoURL, it.following, it.followers
            )

            Log.d("@@User to see", "${user}")
            for (followers in userTemp.following!!) {
                if (followers == user.uid) {
                    iFollow = true
                    Log.d("@@Followers", "${followers} ${iFollow}")
                    break
                }
                Log.d("@@Followersout", followers)
            }
            binding.btnFollow.text = "Bekleyiniz"
            if (iFollow) {
                binding.btnFollow.text = "Takip Ediliyor"
            } else {
                binding.btnFollow.text = "Takip Et"
            }
        }
        binding.tvFollowers.text = user.followers?.size.toString()
        binding.tvFollowing.text = user.following?.size.toString()

        binding.btnFollow.setOnClickListener {
            if (binding.btnFollow.text == "Followed") {

                userTemp.following?.remove(user.uid!!)
                user.followers?.remove(userTemp.uid!!)
                viewModel.saveUser(userTemp)
                viewModel.saveUser(user)
                binding.btnFollow.text = "Follow"
                binding.tvFollowing.text = user.following?.size.toString()
                startActivity(Intent(this, HomeActivity::class.java))
            } else {

                userTemp.following?.add(user.uid!!)
                user.followers?.add(userTemp.uid!!)
                viewModel.saveUser(userTemp)
                viewModel.saveUser(user)
                binding.btnFollow.text = "Followed"
                binding.tvFollowers.text = user.followers?.size.toString()
                startActivity(Intent(this, HomeActivity::class.java))
            }

        }

        itemList = ArrayList()
        recyclerView = binding.postRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        db.whereEqualTo("uid", user.uid).get().addOnSuccessListener {
            val list: List<DocumentSnapshot> = it.documents
            for (data in list) {
                val post: PostModel? = data.toObject(PostModel::class.java)
                if (post != null) {
                    itemList.add(post)
                } else {
                    Toast.makeText(
                        this@UsersProfile,
                        "Gönderi kısmını boş bırakamazsınız",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                adapter.notifyItemInserted(itemList.size - 1)
            }
        }
        adapter = ProfilePostAdapter(itemList)
        recyclerView.adapter = adapter
    }
}
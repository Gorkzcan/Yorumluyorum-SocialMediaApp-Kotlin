package com.gorkemozcan.yorumluyorum.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gorkemozcan.yorumluyorum.R
import com.gorkemozcan.yorumluyorum.adapter.ProfilePostAdapter
import com.gorkemozcan.yorumluyorum.databinding.FragmentProfileBinding
import com.gorkemozcan.yorumluyorum.model.PostModel
import com.gorkemozcan.yorumluyorum.view.activities.MainActivity
import com.gorkemozcan.yorumluyorum.view.activities.UserDetailActivity
import com.gorkemozcan.yorumluyorum.viewmodel.FirebaseViewModel

class ProfileFragment  : Fragment(){
    lateinit var binding: FragmentProfileBinding
    lateinit var auth: FirebaseAuth
    lateinit var viewModel: FirebaseViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ProfilePostAdapter
    private var itemList = ArrayList<PostModel>()
    lateinit var imageUri: Uri
    var db = Firebase.firestore.collection("Posts")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(activity!!)[FirebaseViewModel::class.java]
        auth = FirebaseAuth.getInstance()

        viewModel.getCurrentUser()
        viewModel.user().observe(viewLifecycleOwner, Observer{
            binding.tvBio.setText(it.bio.toString())
            binding.profile1.setText(it.userName.toString())
            binding.tvName.setText(it.fullname.toString())
            Glide.with(this).load(it.photoURL).into(binding.ProfilePic)
            binding.tvFollowers.text = it.followers?.size.toString()
            binding.tvFollowing.text = it.following?.size.toString()
        })

        /*binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, MainActivity::class.java))
            activity!!.finish()
        }*/
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(context, UserDetailActivity::class.java))
        }

        //Recyclerview withouth viewModel
        itemList = ArrayList()
        recyclerView = binding.postRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)

        db.whereEqualTo("uid", auth.currentUser?.uid).get().addOnSuccessListener {
            val list: List<DocumentSnapshot> = it.documents
            for(data in list){
                val post: PostModel? = data.toObject(PostModel::class.java)
                if (post != null){
                    itemList.add(post)
                    Log.d("@@User", post.description.toString())
                }
                else{
                    Log.d("@@Post", "Null")
                }

                adapter.notifyItemInserted(itemList.size - 1)
            }
        }
        adapter = ProfilePostAdapter(itemList)
        recyclerView.adapter = adapter


        return binding.root
    }
}
package com.gorkemozcan.yorumluyorum.view.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorkemozcan.yorumluyorum.view.activities.MainActivity
import com.gorkemozcan.yorumluyorum.adapter.FeedPostAdapter
import com.gorkemozcan.yorumluyorum.adapter.ProfilePostAdapter
import com.gorkemozcan.yorumluyorum.adapter.UserListAdapter
import com.gorkemozcan.yorumluyorum.databinding.FragmentFeedBinding
import com.gorkemozcan.yorumluyorum.model.PostModel
import com.gorkemozcan.yorumluyorum.model.UserModel
import com.gorkemozcan.yorumluyorum.viewmodel.FirebaseViewModel
import com.gorkemozcan.yorumluyorum.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AbsListView

class FeedFragment :Fragment(){
    lateinit var binding: FragmentFeedBinding
    private var userList = ArrayList<UserModel>()
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewPosts: RecyclerView
    lateinit var adapter: UserListAdapter
    lateinit var adapterFeedProfile: FeedPostAdapter
    lateinit var viewModel : PostViewModel
    var db = Firebase.firestore.collection("User")
    var dbPost = Firebase.firestore.collection("Posts")
    lateinit var auth: FirebaseAuth
    private var itemList = ArrayList<PostModel>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(layoutInflater, container, false)
        userList = ArrayList()
        recyclerView = binding.feedRecyclerView
        recyclerViewPosts = binding.postRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerViewPosts.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProvider(activity!!)[PostViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        db.get().addOnSuccessListener {
            val list: List<DocumentSnapshot> = it.documents
            for(user in list){
                val User: UserModel? = user.toObject(UserModel::class.java)
                if(User != null){
                    userList.add(User)
                }
            }
        }

        val tempArrayList = ArrayList<UserModel>()
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tempArrayList.clear()
                val searchText = p0!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    userList.forEach {
                        if(it.fullname?.lowercase(Locale.getDefault())?.contains(searchText)!! && it.userName?.lowercase(Locale.getDefault())?.contains(searchText)!!){
                            tempArrayList.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }else{
                    tempArrayList.clear()
                    adapter.notifyDataSetChanged()
                }
                return false
            }
        })
        adapter = UserListAdapter(tempArrayList)
        viewModel.getPosts()

        viewModel.posts().observe(viewLifecycleOwner,Observer{
            itemList = it
            adapterFeedProfile.updateAdapter(itemList)
        })

        adapterFeedProfile = FeedPostAdapter(itemList)
        recyclerViewPosts.adapter = adapterFeedProfile
        recyclerView.adapter = adapter
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                if(!binding.svSearch.isIconified){
                    binding.svSearch.isIconified = true
                }else{
                    activity!!.finish()
                }
            }
        })

        return binding.root
    }


}
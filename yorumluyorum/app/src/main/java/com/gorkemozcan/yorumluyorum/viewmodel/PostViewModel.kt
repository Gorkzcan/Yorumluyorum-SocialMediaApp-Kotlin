package com.gorkemozcan.yorumluyorum.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gorkemozcan.yorumluyorum.model.PostModel
import com.gorkemozcan.yorumluyorum.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application)  {
    var db = Firebase.firestore.collection("User")
    var dbPost = Firebase.firestore.collection("Posts")
    var auth  : FirebaseAuth = FirebaseAuth.getInstance()
    private var currentPosts = MutableLiveData<ArrayList<PostModel>>()
    fun posts() : MutableLiveData<ArrayList<PostModel>> {
        return currentPosts
    }

    fun getPosts() = CoroutineScope(Dispatchers.IO).launch{
        var listOfFollowers = ArrayList<String>()
        var itemList = ArrayList<PostModel>()
        val postList = ArrayList<PostModel>()
        db.get().addOnSuccessListener {
            val list: List<DocumentSnapshot> = it.documents
            for (currUser in list) {

                val User: UserModel? = currUser.toObject(UserModel::class.java)
                if (User?.uid == auth.currentUser?.uid) {
                    listOfFollowers = User?.following!!
                    itemList = ArrayList()
                    Log.d("followingSize", listOfFollowers.size.toString())
                    for (Following in listOfFollowers) {
                        dbPost.whereEqualTo("uid", Following).get().addOnSuccessListener {
                            val list: List<DocumentSnapshot> = it.documents
                            Log.d("Fetching", list.toString())
                            for (data in list) {
                                val post: PostModel? = data.toObject(PostModel::class.java)
                                if (post != null) {
                                    Log.d("@@User", post.uid.toString())

                                    postList.add(post)

                                    Log.d("@@post", "${currentPosts}")
                                } else {
                                    Log.d("Post", "Null")
                                    Toast.makeText(getApplication(), "Bir hata meydana geldi lütfen tekrar deneyiniz", Toast.LENGTH_SHORT).show()
                                }

                            }
                            currentPosts.postValue(postList)
                        }
                    }

                    break
                }
            }

        }



    }




}
package com.gorkemozcan.yorumluyorum.adapter

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gorkemozcan.yorumluyorum.R
import com.gorkemozcan.yorumluyorum.view.activities.UsersProfile
import com.gorkemozcan.yorumluyorum.model.PostModel
import com.google.firebase.firestore.auth.User
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide
import com.gorkemozcan.yorumluyorum.view.activities.HomeActivity
import com.gorkemozcan.yorumluyorum.model.LikeModel
import com.gorkemozcan.yorumluyorum.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mikhaellopez.circularimageview.CircularImageView

class FeedPostAdapter(private var posts: ArrayList<PostModel>):RecyclerView.Adapter<FeedPostAdapter.ViewHolder>() {
    var likedUser = LikeModel()
    lateinit var auth: FirebaseAuth
    private lateinit var imageUri:Uri

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        auth = FirebaseAuth.getInstance()
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return FeedPostAdapter.ViewHolder(itemView)
    }

    class ViewHolder(row: View): RecyclerView.ViewHolder(row){
        var postImage : ImageView = row.findViewById(R.id.item_iv)
        var postProfilePic : ImageView = row.findViewById(R.id.iv_itemProfilePic)
        var postDescription : TextView = row.findViewById(R.id.item_description)
       // var postLikes : ImageView = row.findViewById(R.id.iv_heart)
        //var postComments : ImageView = row.findViewById(R.id.iv_comments)
        //var postNumberLikes : TextView = row.findViewById(R.id.tv_likes)
        //var postNumberComments : TextView = row.findViewById(R.id.tv_comments)
        var postUserName : TextView = row.findViewById(R.id.iv_itemUserName)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.postUserName.text = post.userName
        Glide.with(holder.itemView.context).load(post.photoURL).into(holder.postProfilePic)
        Glide.with(holder.itemView.context).load(post.imageUrl).into(holder.postImage)
        holder.postDescription.text = post.description
    }

    fun updateAdapter(mPosts: ArrayList<PostModel>){
        this.posts = mPosts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return posts.size
    }


}
package com.gorkemozcan.yorumluyorum.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gorkemozcan.yorumluyorum.model.PostModel
import com.gorkemozcan.yorumluyorum.R
import com.gorkemozcan.yorumluyorum.view.activities.UsersProfile
import com.gorkemozcan.yorumluyorum.view.fragments.FeedFragment
import com.gorkemozcan.yorumluyorum.model.UserModel

class ProfilePostAdapter (private var postsList: ArrayList<PostModel>):RecyclerView.Adapter<ProfilePostAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var postImage : ImageView = itemView.findViewById(R.id.item_iv)
        var postDescription: TextView = itemView.findViewById(R.id.item_description)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postsList[position]
        Glide.with(holder.itemView.context).load(post.imageUrl).into(holder.postImage)
        holder.postDescription.text = post.description
    }
    fun updateAdapter(mPosts: ArrayList<PostModel>){
        this.postsList = mPosts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

}
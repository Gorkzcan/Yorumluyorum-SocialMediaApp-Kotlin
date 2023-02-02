package com.gorkemozcan.yorumluyorum.adapter

import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gorkemozcan.yorumluyorum.R
import com.gorkemozcan.yorumluyorum.model.UserModel
import com.gorkemozcan.yorumluyorum.view.activities.UsersProfile

class UserListAdapter(private var userList: ArrayList<UserModel>):RecyclerView.Adapter<UserListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): UserListAdapter.ViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserListAdapter.ViewHolder, position: Int) {
        val User = userList[position]
        Glide.with(holder.itemView.context).load(User.photoURL).into(holder.profilePic)
        holder.fullName.text = User.fullname
        holder.username.text = User.userName

        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            val send = Intent(context, UsersProfile::class.java)
            send.putExtra("User", User)
            context.startActivity(send)
        }
    }

    fun updateAdapter(mUser: ArrayList<UserModel>){
        this.userList = mUser
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val profilePic: ImageView = itemView.findViewById(R.id.iv_itemProfilePic)
        val fullName: TextView = itemView.findViewById(R.id.tv_itemName)
        val username: TextView = itemView.findViewById(R.id.tv_userName)
    }

}
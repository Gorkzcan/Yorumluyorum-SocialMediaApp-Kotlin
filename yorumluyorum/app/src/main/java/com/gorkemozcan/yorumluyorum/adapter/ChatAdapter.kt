package com.gorkemozcan.yorumluyorum.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gorkemozcan.yorumluyorum.R
import com.gorkemozcan.yorumluyorum.model.UserModel
import com.gorkemozcan.yorumluyorum.view.activities.ChatActivity

class ChatAdapter(private var userList: ArrayList<UserModel>):RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val profilePic : ImageView
        val fullName: TextView
        val userName: TextView

        init {
            this.profilePic = view.findViewById(R.id.iv_itemProfilePic)
            this.fullName = view.findViewById(R.id.tv_itemName)
            this.userName = view.findViewById(R.id.tv_userName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ChatAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val User = userList[position]
        Glide.with(holder.itemView.context).load(User.photoURL).into(holder.profilePic)
        holder.fullName.text = User.fullname
        holder.userName.text = User.userName
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            val send = Intent(context, ChatActivity::class.java)
            send.putExtra("User", User)
            context.startActivity(send)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateAdapter(mUser: ArrayList<UserModel>){
        this.userList= mUser
        notifyDataSetChanged()
    }


}
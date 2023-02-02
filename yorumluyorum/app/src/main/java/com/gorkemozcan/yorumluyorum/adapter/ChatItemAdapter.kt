package com.gorkemozcan.yorumluyorum.adapter

import android.text.Layout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.gorkemozcan.yorumluyorum.R
import com.gorkemozcan.yorumluyorum.model.ChatModel

class ChatItemAdapter(private var chatList:ArrayList<ChatModel>):RecyclerView.Adapter<ChatItemAdapter.ViewHolder>() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val date: TextView
        val message: TextView
        val layoutP : FrameLayout
        val layout: ConstraintLayout
        init {
            this.message = view.findViewById(R.id.message)
            this.date = view.findViewById(R.id.tv_Date)
            this.layoutP = view.findViewById(R.id.parent_layout)
            this.layout = view.findViewById(R.id.layout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item,parent,false)
        return ChatItemAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentChat = chatList[position]
        if (currentChat.senderId == auth.currentUser?.uid){
            holder.layout.apply {
                setBackgroundResource(R.drawable.chat_shape_primary)
                val lParms = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.END).apply {
                    bottomMargin = 10
                    topMargin = 10
                    leftMargin = 10
                    rightMargin = 10
                }
                this.layoutParams = lParms
            }
        }
        holder.message.text = currentChat.chat
        holder.date.text = currentChat.date
    }

    fun updateAdapter(mChat: ArrayList<ChatModel>){
        this.chatList = mChat
        notifyDataSetChanged()
    }

    fun clearAdapter(){
        this.chatList.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return chatList.size
    }


}
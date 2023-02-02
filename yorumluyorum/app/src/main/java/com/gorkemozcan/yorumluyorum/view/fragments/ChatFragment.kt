package com.gorkemozcan.yorumluyorum.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorkemozcan.yorumluyorum.R
import com.gorkemozcan.yorumluyorum.adapter.ChatAdapter
import com.gorkemozcan.yorumluyorum.databinding.FragmentChatBinding
import com.gorkemozcan.yorumluyorum.model.UserModel
import com.gorkemozcan.yorumluyorum.viewmodel.ChatViewModel

class ChatFragment: Fragment(){
    lateinit var binding: FragmentChatBinding
    lateinit var recyclerView :RecyclerView
    lateinit var adapter: ChatAdapter
    lateinit var viewModel : ChatViewModel
    private var itemList = ArrayList<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        recyclerView = binding.chatUserRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]

        viewModel.following().observe(viewLifecycleOwner, Observer {
            itemList = it
            adapter.updateAdapter(itemList)
        })

        adapter = ChatAdapter(itemList)
        recyclerView.adapter = adapter
        return binding.root
    }
}
package com.gorkemozcan.yorumluyorum.service

import com.gorkemozcan.yorumluyorum.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit {
    companion object{
        private val retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }


}
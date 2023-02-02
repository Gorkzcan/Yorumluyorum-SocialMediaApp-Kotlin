package com.gorkemozcan.yorumluyorum.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostModel(
    var uid: String? = null,
    var imageUrl: String?= null,
    var description: String?=null,
    var fullName:String? =null,
    var userName:String?=null,
    var photoURL:String?=null,
    var time:Long?=null,
    var likes:Long?=null
):Parcelable
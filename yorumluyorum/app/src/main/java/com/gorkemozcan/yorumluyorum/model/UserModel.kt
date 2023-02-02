package com.gorkemozcan.yorumluyorum.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel (
    var uid: String? =null,
    var fullname : String? = null,
    var userName: String? =null,
    var bio :String?=null,
    var photoURL :String?=null,
    var following :ArrayList<String>? = arrayListOf(),
    var followers: ArrayList<String>?= arrayListOf()
): Parcelable
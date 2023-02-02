package com.gorkemozcan.yorumluyorum.model

data class CommentModel(
    var message : String = "",
    var uid :  String?= null,
    var photoUrl : String? = null,
    var fullName : String? = null
)
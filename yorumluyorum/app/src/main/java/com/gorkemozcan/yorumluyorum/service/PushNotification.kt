package com.gorkemozcan.yorumluyorum.service

data class PushNotification(
    val data : NotificationModel,
    val to : String
)
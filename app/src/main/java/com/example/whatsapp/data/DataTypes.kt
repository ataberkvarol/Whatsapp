package com.example.whatsapp.data

data class ChatUser(
        val userId: String? = "",
        val name: String? = "",
        val number: String? = "",
        val imageUrl: String? = "",
    )
data class Status(
        val user: ChatUser = ChatUser(),
        val imageUrl: String? = "",
        val timestamp: Long? = null
    )
data class Message(
        val sentBy:String? = "",
        val message:String? = "",
        val timestamp: String? = " "
    )
data class ChatData(
        val chatId:String? = "",
        val user1: ChatUser = ChatUser(),
        val user2: ChatUser = ChatUser()
)


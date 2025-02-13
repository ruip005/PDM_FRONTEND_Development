package com.example.datingapp.API.Endpoints

import com.example.datingapp.Classes.MessageItem

data class Message(
    val apiSenderName: String,
    val apiMessageText: String,
    val apiDateTime: String
) : MessageItem {
    override val senderName: String get() = apiSenderName
    override val messageText: String get() = apiMessageText
    override val dateTime: String get() = apiDateTime
}

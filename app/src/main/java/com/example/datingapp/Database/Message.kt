package com.example.datingapp.Database

import java.util.Date

data class Message(
    val senderName: String,
    val message: String,
    val dateTime: Date
)
package com.example.datingapp.API.Endpoints

import java.util.Date

data class Message(
    val senderName: String,
    val message: String,
    val dateTime: Date
)
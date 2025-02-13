package com.example.datingapp.API.Endpoints

data class ApiMessage(
    val senderName: String,
    val message: String,
    val dateTime: String // A API espera o timestamp como String
)

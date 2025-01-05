package com.example.datingapp.API.Endpoints

data class LoginResponse(
    val success: Boolean,
    val token: String,
    val message: String
)
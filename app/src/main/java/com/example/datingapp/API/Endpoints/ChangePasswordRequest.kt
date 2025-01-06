package com.example.datingapp.API.Endpoints

data class ChangePasswordRequest(
    val password: String,
    val newpassword: String
)
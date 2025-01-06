package com.example.datingapp.API.Endpoints

data class UpdateUserRequest(
    val guid: String,
    val name: String,
    val email: String,
    val phone: String,
)

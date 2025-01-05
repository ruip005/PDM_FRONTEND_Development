package com.example.datingapp.API.Endpoints

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val gender: String,
    val birthdate: String
)

package com.example.datingapp.API.Endpoints

data class Message(
    //val Id: Int, // Adicionado campo Id
    val SenderName: String, // Ajustado para corresponder ao JSON
    val Message: String, // Ajustado para corresponder ao JSON
    val DateTime: String // Ajustado para String para lidar com o formato ISO 8601
)
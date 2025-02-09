package com.example.datingapp.API.Endpoints

data class UploadProfileResponse(
    val success: Boolean,
    val message: String,
    val data: UploadedImageData? = null
)

data class UploadedImageData(
    val id: Int,
    val binary: ByteArray, // Binary Data
    val updatedAt: String,
    val createdAt: String
)

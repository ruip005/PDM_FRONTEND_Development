package com.example.datingapp.Classes

data class UserPhotos(
    val id: Int, // ID da foto
    val userId: Int, // ID do utilizador
    val photoBinary: ByteArray // Dados binários da foto
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserPhotos

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (!photoBinary.contentEquals(other.photoBinary)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + userId
        result = 31 * result + photoBinary.contentHashCode()
        return result
    }
}
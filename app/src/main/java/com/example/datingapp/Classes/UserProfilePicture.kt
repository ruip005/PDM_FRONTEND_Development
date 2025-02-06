package com.example.datingapp.Classes

data class UserProfilePicture(
    val id: Int, // ID do utilizador
    val binary: ByteArray // Dados binários da foto de perfil
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserProfilePicture

        if (id != other.id) return false
        if (!binary.contentEquals(other.binary)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + binary.contentHashCode()
        return result
    }
}
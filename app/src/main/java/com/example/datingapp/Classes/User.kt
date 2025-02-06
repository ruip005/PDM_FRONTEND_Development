package com.example.datingapp.Classes

import java.util.*

data class User(
    val id: Int, // ID do utilizador
    val guid: UUID, // GUID do utilizador
    val name: String, // Nome do utilizador
    val email: String, // Email do utilizador
    val password: String, // Senha do utilizador
    val gender: String?, // Gênero (M ou F, pode ser nulo)
    val birthdate: Date?, // Data de nascimento (pode ser nulo)
    val phone: Int?, // Telefone (pode ser nulo)
    val isActive: Boolean, // Status de ativação
    val createdOn: Date, // Data de criação
    val updatedOn: Date?, // Data de atualização (pode ser nulo)
    val profilePicture: UserProfilePicture?, // Foto de perfil (pode ser nulo)
    val photos: List<UserPhotos>?, // Fotos adicionais (pode ser nulo)
    val profileDetail: ProfileDetail? // Detalhes adicionais do perfil (pode ser nulo)
)


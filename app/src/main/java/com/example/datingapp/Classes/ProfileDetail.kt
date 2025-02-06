package com.example.datingapp.Classes

data class ProfileDetail(
    val id: Int, // ID do utilizador
    val height: Int?, // Altura (pode ser nulo)
    val qualifications: String?, // Qualificações (pode ser nulo)
    val sign: String?, // Signo (pode ser nulo)
    val wantChilds: String?, // Desejo de ter filhos (pode ser nulo)
    val styleOfCommunication: String?, // Estilo de comunicação (pode ser nulo)
    val typeOfLove: String?, // Tipo de amor (pode ser nulo)
    val lookingFor: String, // O que está à procura (não pode ser nulo)
    val animals: String?, // Animais (pode ser nulo)
    val smoking: String?, // Fumo (pode ser nulo)
    val drinking: String?, // Bebida (pode ser nulo)
    val gym: String?, // Ginásio (pode ser nulo)
    val sexualOrientation: String?, // Orientação sexual (pode ser nulo)
    val school: String? // Escola (pode ser nulo)
)
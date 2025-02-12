package com.example.datingapp.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderName: String,
    val message: String,
    val dateTime: Long // Alterado de Date para Long
)

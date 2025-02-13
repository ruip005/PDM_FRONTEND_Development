package com.example.datingapp.Classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class GenericMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    override var senderName: String,
    override var messageText: String,
    override var dateTime: String
) : MessageItem

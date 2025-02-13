package com.example.datingapp.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.datingapp.Classes.MessageItem

@Entity(tableName = "messages")
data class LocalMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dbSenderName: String,
    val dbMessageText: String,
    val dbDateTime: String
) : MessageItem {
    override val senderName: String get() = dbSenderName
    override val messageText: String get() = dbMessageText
    override val dateTime: String get() = dbDateTime
}

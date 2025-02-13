package com.example.datingapp.Utils

import com.example.datingapp.Database.DatabaseMessage
import com.example.datingapp.API.Endpoints.ApiMessage
import com.example.datingapp.API.Endpoints.Message

fun DatabaseMessage.toApiMessage(): ApiMessage {
    return ApiMessage(
        senderName = this.senderName,
        message = this.message,
        dateTime = this.dateTime.toString()
    )
}

fun ApiMessage.toDatabaseMessage(): DatabaseMessage {
    return DatabaseMessage(
        senderName = this.senderName,
        message = this.message,
        dateTime = this.dateTime.toLongOrNull() ?: System.currentTimeMillis(),
        isSent = true
    )
}

fun ApiMessage.toMessage(): Message {
    return Message(
        SenderName = this.senderName,
        Message = this.message,
        DateTime = this.dateTime
    )
}

fun DatabaseMessage.toMessage(): Message {
    return Message(
        SenderName = this.senderName,
        Message = this.message,
        DateTime = this.dateTime.toString()
    )
}

fun Message.toDatabaseMessage(): DatabaseMessage {
    return DatabaseMessage(
        senderName = this.SenderName,
        message = this.Message,
        dateTime = this.DateTime.toLongOrNull() ?: System.currentTimeMillis(),
        isSent = false
    )
}

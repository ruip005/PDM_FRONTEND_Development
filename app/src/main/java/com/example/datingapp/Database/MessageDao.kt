package com.example.datingapp.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: Message)

    @Query("SELECT * FROM messages ORDER BY dateTime DESC")
    suspend fun getAllMessages(): List<Message>

    @Query("DELETE FROM messages")
    suspend fun deleteAll()
}

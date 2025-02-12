package com.example.datingapp.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: Message)

    @Insert
    suspend fun insertAll(messages: List<Message>) // Adicione este método

    @Query("SELECT * FROM messages ORDER BY dateTime ASC")
    fun getAllMessages(): LiveData<List<Message>>
}
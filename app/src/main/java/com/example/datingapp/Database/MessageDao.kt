package com.example.datingapp.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.datingapp.Database.Message

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: Message)

    @Insert
    suspend fun insertAll(messages: List<Message>) // Adicione este método

    @Query("SELECT * FROM messages ORDER BY dateTime ASC")
    fun getAllMessages(): LiveData<List<Message>> // Certifica-te que não há nullable types


}
package com.example.datingapp.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.datingapp.Classes.GenericMessage

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: GenericMessage)

    @Query("SELECT * FROM messages ORDER BY dateTime DESC")
    suspend fun getAllMessages(): List<GenericMessage>

    @Query("DELETE FROM messages")
    suspend fun deleteAll()
}

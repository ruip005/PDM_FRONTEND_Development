package com.example.datingapp.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: DatabaseMessage)

    @Insert
    suspend fun insertAll(messages: List<DatabaseMessage>)

    @Query("SELECT * FROM messages ORDER BY dateTime ASC")
    fun getAllMessages(): LiveData<List<DatabaseMessage>>

    @Query("SELECT * FROM messages WHERE isSent = 0")
    suspend fun getUnsentMessages(): List<DatabaseMessage>

    @Query("UPDATE messages SET isSent = 1 WHERE id = :messageId")
    suspend fun markAsSent(messageId: Int)
}

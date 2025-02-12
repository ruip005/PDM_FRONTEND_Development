package com.example.datingapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.datingapp.Database.AppDatabase
import com.example.datingapp.Database.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val messageDao = database.messageDao()

    val allMessages: LiveData<List<Message>> = messageDao.getAllMessages()

    fun insertMessage(message: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            messageDao.insert(message)
        }
    }

    fun insertMessages(messages: List<Message>) {
        CoroutineScope(Dispatchers.IO).launch {
            messageDao.insertAll(messages)
        }
    }
}
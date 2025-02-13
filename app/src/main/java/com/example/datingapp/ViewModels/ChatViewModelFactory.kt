package com.example.datingapp.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.datingapp.Database.MessageDao

class ChatViewModelFactory(private val messageDao: MessageDao, private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(messageDao, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

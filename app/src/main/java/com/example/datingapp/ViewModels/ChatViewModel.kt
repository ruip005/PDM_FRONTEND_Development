package com.example.datingapp.ViewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datingapp.API.ApiClient
import com.example.datingapp.API.Endpoints.Message
import com.example.datingapp.Database.DatabaseMessage
import com.example.datingapp.Database.MessageDao
import com.example.datingapp.Utils.NetworkUtils
import com.example.datingapp.Utils.toApiMessage
import com.example.datingapp.Utils.toMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(private val messageDao: MessageDao, private val context: Context) : ViewModel() {

    private val _messages = MutableLiveData<List<DatabaseMessage>>()
    val messages: LiveData<List<DatabaseMessage>> get() = _messages

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun loadMessages() {
        _messages.value = messageDao.getAllMessages().value
    }

    fun sendMessage(message: DatabaseMessage) {
        viewModelScope.launch {
            messageDao.insert(message)

            if (NetworkUtils.isConnected(context)) {
                val apiMessage = message.toApiMessage().toMessage() // Conversão correta para Message
                ApiClient.sendMessage(apiMessage) { response, error ->
                    if (error == null) {
                        viewModelScope.launch {
                            withContext(Dispatchers.IO) {
                                messageDao.markAsSent(message.id)
                            }
                        }
                    }
                }
            }
        }
    }

    fun retrySendingUnsentMessages() {
        viewModelScope.launch {
            val unsentMessages = withContext(Dispatchers.IO) { messageDao.getUnsentMessages() }
            for (msg in unsentMessages) {
                val apiMessage = msg.toApiMessage().toMessage() // Conversão correta
                ApiClient.sendMessage(apiMessage) { response, error ->
                    if (error == null) {
                        viewModelScope.launch {
                            withContext(Dispatchers.IO) {
                                messageDao.markAsSent(msg.id)
                            }
                        }
                    }
                }
            }
        }
    }
}

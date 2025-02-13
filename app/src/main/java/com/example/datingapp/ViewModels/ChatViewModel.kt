package com.example.datingapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.datingapp.API.ApiClient
import com.example.datingapp.Classes.GenericMessage
import com.example.datingapp.Database.MessageDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val _messages = MutableLiveData<List<GenericMessage>>()
    val messages: LiveData<List<GenericMessage>> get() = _messages

    private val _localMessages = MutableLiveData<List<GenericMessage>>()
    val localMessages: LiveData<List<GenericMessage>> get() = _localMessages

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val messageDao = MessageDatabase.getDatabase(application).messageDao()

    fun loadMessages() {
        ApiClient.getMessages { apiMessages, error ->
            if (error != null) {
                _errorMessage.value = error ?: "Erro desconhecido"
            } else {
                _messages.value = apiMessages ?: emptyList()
            }
        }
    }

    fun loadLocalMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            val localMessagesList = messageDao.getAllMessages()
            _localMessages.postValue(localMessagesList)
        }
    }

    fun sendMessage(message: GenericMessage) {
        ApiClient.sendMessage(message) { response, error ->
            if (error != null) {
                _errorMessage.value = error ?: "Erro desconhecido"
            } else {
                loadMessages()
            }
        }
    }

    fun saveMessageOffline(message: GenericMessage) {
        viewModelScope.launch(Dispatchers.IO) {
            messageDao.insert(message)
        }
    }
}

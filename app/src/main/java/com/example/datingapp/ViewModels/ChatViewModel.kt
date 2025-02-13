package com.example.datingapp.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.datingapp.API.ApiClient
import com.example.datingapp.API.Endpoints.Message

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun loadMessages() {
        ApiClient.getMessages { messages, error ->
            if (error != null) {
                _errorMessage.value = error ?: "Unknown error" // Notifica sobre o erro
            } else {
                _messages.value = messages ?: emptyList() // Atualiza a lista de mensagens
            }
        }
    }

    fun sendMessage(message: Message) {
        ApiClient.sendMessage(message) { response, error ->
            if (error != null) {
                _errorMessage.value = error ?: "Unknown error" // Notifica sobre o erro
            } else {
                // Recarrega as mensagens após enviar uma nova
                loadMessages()
            }
        }
    }
}
package com.example.datingapp.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.API.Endpoints.Message
import com.example.datingapp.R
import com.example.datingapp.ViewModels.ChatViewModel
import com.example.datingapp.adapters.MessageAdapter
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        val sendButton = findViewById<Button>(R.id.sendButton)
        val messageInput = findViewById<EditText>(R.id.messageInput)

        // Configura o RecyclerView
        messageAdapter = MessageAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }

        // Observa as mensagens da API
        viewModel.messages.observe(this, Observer { messages ->
            if (messages != null) {
                messageAdapter.submitList(messages)
            } else {
                messageAdapter.submitList(emptyList()) // Evita crash ao carregar a UI vazia
            }
        })

        // Observa erros
        viewModel.errorMessage.observe(this, Observer { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        })

        // Carrega as mensagens ao iniciar a atividade
        viewModel.loadMessages()

        // Envia a mensagem
        sendButton.setOnClickListener {
            val senderName = "User" // Substitua pelo nome do usuário
            val messageText = messageInput.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(senderName, messageText, Date().toString())
                viewModel.sendMessage(message)
                messageInput.text.clear()
            } else {
                Toast.makeText(this, "Digite uma mensagem", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
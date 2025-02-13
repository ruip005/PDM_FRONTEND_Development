package com.example.datingapp.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.API.Endpoints.Message
import com.example.datingapp.Database.AppDatabase
import com.example.datingapp.Database.DatabaseMessage
import com.example.datingapp.R
import com.example.datingapp.Utils.DataUtils
import com.example.datingapp.Utils.DialogUtils
import com.example.datingapp.Utils.toMessage
import com.example.datingapp.Utils.toDatabaseMessage
import com.example.datingapp.ViewModels.ChatViewModel
import com.example.datingapp.ViewModels.ChatViewModelFactory
import com.example.datingapp.adapters.MessageAdapter
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val database = AppDatabase.getDatabase(applicationContext)
        val messageDao = database.messageDao()
        val factory = ChatViewModelFactory(messageDao, applicationContext)
        viewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        val sendButton = findViewById<Button>(R.id.sendButton)
        val messageInput = findViewById<EditText>(R.id.messageInput)
        val refreshButton = findViewById<Button>(R.id.refreshButton)

        // Configura o RecyclerView
        messageAdapter = MessageAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }

        // Observa as mensagens da API e converte DatabaseMessage para Message
        viewModel.messages.observe(this, Observer { messages ->
            if (messages != null) {
                val convertedMessages = messages.map { it.toMessage() }
                messageAdapter.submitList(convertedMessages)
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

        refreshButton.setOnClickListener {
            viewModel.loadMessages() // Recarrega as mensagens
            DialogUtils.showSuccessToast(this, "Mensagens atualizadas")
        }

        // Envia a mensagem
        sendButton.setOnClickListener {
            var senderName = "User"
            // Buscar o nome no token de autenticação
            val jwtPayload = DataUtils.parseJwt(this)
            if (jwtPayload != null) {
                senderName = jwtPayload.getString("name")
            }
            val messageText = messageInput.text.toString()
            if (messageText.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(Date())

                val message = Message(senderName, messageText, formattedDate)
                val databaseMessage = message.toDatabaseMessage() // Converter para DatabaseMessage
                viewModel.sendMessage(databaseMessage) // Agora o tipo é correto

                messageInput.text.clear()
            } else {
                Toast.makeText(this, "Digite uma mensagem", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

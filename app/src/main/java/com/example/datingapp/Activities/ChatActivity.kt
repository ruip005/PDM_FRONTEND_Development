package com.example.datingapp.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.Classes.GenericMessage
import com.example.datingapp.R
import com.example.datingapp.Utils.DataUtils
import com.example.datingapp.Utils.DialogUtils
import com.example.datingapp.Utils.isOnline
import com.example.datingapp.ViewModels.ChatViewModel
import com.example.datingapp.adapters.MessageAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

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

        // Observa as mensagens da API
        viewModel.messages.observe(this, Observer { messages ->
            if (messages != null) {
                messageAdapter.submitList(messages)
            } else {
                messageAdapter.submitList(emptyList()) // Evita crash ao carregar a UI vazia
            }
        })

        // Observa as mensagens locais
        viewModel.localMessages.observe(this, Observer { localMessages ->
            if (localMessages != null) {
                messageAdapter.submitList(localMessages)
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
        if (isOnline(this)) {
            viewModel.loadMessages()
        } else {
            viewModel.loadLocalMessages()
        }

        refreshButton.setOnClickListener {
            if (isOnline(this)) {
                viewModel.loadMessages() // Recarrega as mensagens
                DialogUtils.showSuccessToast(this, "Mensagens atualizadas")
            } else {
                viewModel.loadLocalMessages() // Carrega mensagens locais quando offline
                DialogUtils.showErrorToast(this, "Sem conexão com a internet. Mensagens locais exibidas.")
            }
        }

        // Envia a mensagem
        sendButton.setOnClickListener {
            val isOnline = isOnline(this)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(Date())
            val messageText = messageInput.text.toString()
            if (messageText.isNotEmpty()) {
                val senderName = DataUtils.parseJwt(this)?.getString("name") ?: "Unknown"
                val genericMessage = GenericMessage(
                    senderName = senderName,
                    messageText = messageText,
                    dateTime = formattedDate
                )

                if (!isOnline) {
                    // Guarda a mensagem offline
                    viewModel.saveMessageOffline(genericMessage)
                    DialogUtils.showErrorToast(this, "Sem conexão com a internet. Mensagem guardada offline.")
                } else {
                    // Envia a mensagem à API
                    viewModel.sendMessage(genericMessage)
                    messageInput.text.clear()
                }
            } else {
                Toast.makeText(this, "Digite uma mensagem", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

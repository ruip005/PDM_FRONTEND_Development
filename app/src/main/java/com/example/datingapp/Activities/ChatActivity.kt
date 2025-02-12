package com.example.datingapp.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datingapp.Database.Message
import com.example.datingapp.R
import com.example.datingapp.ViewModels.ChatViewModel
import com.example.datingapp.adapters.MessageAdapter
import com.example.datingapp.databinding.ActivityChatBinding
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o View Binding
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        // Configura o RecyclerView
        messageAdapter = MessageAdapter()
        binding.recyclerView1.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }

        // Observa as mensagens do banco de dados
        viewModel.allMessages.observe(this, Observer { messages ->
            messageAdapter.submitList(messages)
        })

        // Envia a mensagem
        binding.sendButton.setOnClickListener {
            val senderName = "User"
            val messageText = binding.messageInput.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(senderName = senderName, message = messageText, dateTime = Date())
                viewModel.insertMessage(message)
                binding.messageInput.text.clear()
            } else {
                Toast.makeText(this, "Digite uma mensagem", Toast.LENGTH_SHORT).show()
            }
        }*/
    }
}

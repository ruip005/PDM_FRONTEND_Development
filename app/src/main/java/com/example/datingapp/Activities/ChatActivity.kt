package com.example.datingapp.Activities

import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.R
import com.example.datingapp.adapters.*

//import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Configurar RecyclerView
        messageAdapter = MessageAdapter(messages)
        /*
        rvMessages.layoutManager = LinearLayoutManager(this)
        rvMessages.adapter = messageAdapter

        // Exemplo de dados (substitua pela lógica real)
        val currentUser = User(id = 1, name = "João")
        val otherUser = User(id = 2, name = "Maria")

        // Exemplo de mensagens
        messages.add(Message(senderId = currentUser.id, receiverId = otherUser.id, message = "Olá!"))
        messages.add(Message(senderId = otherUser.id, receiverId = currentUser.id, message = "Oi!"))

        // Atualizar a lista de mensagens
        messageAdapter.notifyDataSetChanged()

        // Configurar botão de enviar
        btnSend.setOnClickListener {
            val messageText = etMessage.text.toString()
            if (messageText.isNotEmpty()) {
                // Enviar mensagem (substitua pela lógica real)
                val message = Message(senderId = currentUser.id, receiverId = otherUser.id, message = messageText)
                messages.add(message)
                messageAdapter.notifyItemInserted(messages.size - 1)

                // Limpar campo de texto
                etMessage.text.clear()
            }
        }*/
    }
}
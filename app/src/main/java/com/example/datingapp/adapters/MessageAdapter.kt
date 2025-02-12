package com.example.datingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.API.Endpoints.Message
import com.example.datingapp.R

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var messages = listOf<Message>() // Usar a classe Message da API

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderName: TextView = itemView.findViewById(R.id.senderName)
        val messageText: TextView = itemView.findViewById(R.id.messageText)
        val dateTime: TextView = itemView.findViewById(R.id.dateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.senderName.text = message.SenderName // Usar SenderName (API)
        holder.messageText.text = message.Message // Usar Message (API)
        holder.dateTime.text = message.DateTime // Usar DateTime (API)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun submitList(newMessages: List<Message>) {
        messages = newMessages // Atribuir a lista diretamente
        notifyDataSetChanged()
    }
}
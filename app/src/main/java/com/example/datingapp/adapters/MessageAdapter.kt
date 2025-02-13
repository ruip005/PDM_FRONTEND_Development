package com.example.datingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.Classes.MessageItem
import com.example.datingapp.R

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var messages = listOf<MessageItem>()

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
        holder.senderName.text = message.senderName
        holder.messageText.text = message.messageText
        holder.dateTime.text = message.dateTime
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun submitList(newMessages: List<MessageItem>) {
        messages = newMessages
        notifyDataSetChanged()
    }
}

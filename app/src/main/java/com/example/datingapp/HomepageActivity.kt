package com.example.datingapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomepageActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Exemplo de botão de like/dislike
        val likeButton = findViewById<Button>(R.id.likeButton)
        val dislikeButton = findViewById<Button>(R.id.dislikeButton)

        likeButton.setOnClickListener {
            // Lógica para dar like yha
        }

        dislikeButton.setOnClickListener {
            // Lógica para dar dislike
        }
    }
}

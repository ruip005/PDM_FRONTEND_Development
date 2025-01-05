package com.example.datingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.Utils.DataUtils
import com.example.datingapp.Utils.DialogUtils

class HomepageActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Exemplo de botão de like/dislike
        val likeButton = findViewById<Button>(R.id.likeButton)
        val dislikeButton = findViewById<Button>(R.id.dislikeButton)
        val logoutButton = findViewById<Button>(R.id.logoutbutton)
        val profileName = findViewById<TextView>(R.id.profileName)

        DataUtils.parseJwt(this)?.let {
            profileName.text = it["name"].toString()
        }

        logoutButton.setOnClickListener {
            logout()
            DialogUtils.showNotification(
                context = this,
                channelId = "logout",
                title = "Logout",
                message = "Sessão terminada com sucesso!"
            )
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        likeButton.setOnClickListener {
            // Lógica para dar like yha
        }

        dislikeButton.setOnClickListener {
            // Lógica para dar dislike
        }
    }

    private fun logout() {
        val sharedPreferences = this.getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("SESSION_TOKEN")
        editor.apply()
    }
}

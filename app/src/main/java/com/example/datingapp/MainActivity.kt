package com.example.datingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar o botão
        /*
        val goToLoginButton = findViewById<Button>(R.id.goToLoginButton)
        goToLoginButton.setOnClickListener {
            // Ir para a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }*/
        if (true) {
            // Ir para a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

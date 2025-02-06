package com.example.datingapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Verifique se o usuário já está logado
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("SESSION_TOKEN", null)

        if (sessionToken != null) {
            // Usuário já está logado, vá direto para a homepage ou outra tela principal
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Mostre a MainActivity (com os botões "Create Account" e "Login")
            setupButtons()
        }
    }

    private fun setupButtons() {
        val createAccountButton = findViewById<Button>(R.id.createAccountButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        createAccountButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
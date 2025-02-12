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

        // Verifica se o utilizador já está logado ao iniciar a aplicação
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("SESSION_TOKEN", null)

        if (sessionToken != null) {
            // Se houver um token de sessão, o utilizador é redirecionado para a página principal (ProfilePage)
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish() // Fecha esta activity para impedir que o utilizador volte à tela inicial pressionando "Voltar"
        } else {
            // Se não houver um token, mantém o utilizador nesta tela e exibe os botões de "Criar Conta" e "Login"
            setupButtons()
        }
    }

    /**
     * Configura os botões "Criar Conta" e "Login" na tela inicial.
     */
    private fun setupButtons() {
        val createAccountButton = findViewById<Button>(R.id.createAccountButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // Redireciona o utilizador para a tela de registo ao clicar em "Criar Conta"
        createAccountButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish() // Fecha esta activity para evitar que o utilizador volte ao ecrã inicial
        }

        // Redireciona o utilizador para a tela de login ao clicar em "Login"
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Fecha esta activity para impedir que o utilizador volte para esta tela ao pressionar "Voltar"
        }
    }
}

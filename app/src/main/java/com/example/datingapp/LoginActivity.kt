package com.example.datingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.API.ApiClient
import com.example.datingapp.API.Endpoints.LoginRequest
import com.example.datingapp.API.Endpoints.LoginResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val token = getSessionToken()

        if (token != null) {
            // Se já tiver um token salvo, vai direto para a homepage
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
            println("Token encontrado: $token")
            finish()
        }

        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val loginButton = findViewById<MaterialButton>(R.id.loginButton)
        val signupTextView = findViewById<TextView>(R.id.signupTextView)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(email, password)

                // Fazendo a requisição de login com Retrofit
                CoroutineScope(Dispatchers.Main).launch {
                    ApiClient.login(loginRequest) { response, error ->
                        if (error != null) {
                            // Exibir erro no log
                            println("Erro: $error")
                            DialogUtils.showErrorPopup(
                                context = this@LoginActivity,
                                title = "Erro de Comunicação",
                                message = "Falha na comunicação com o servidor."
                            )
                        } else if (response != null) {
                            // Analisar a resposta
                            val success = response.success
                            val token = response.token

                            if (success && token != null) {
                                // Guardar o token no SharedPreferences
                                saveSessionToken(token)
                                println("Token salvo: $token")

                                // Navegar para a próxima página
                                val intent = Intent(this@LoginActivity, HomepageActivity::class.java)
                                startActivity(intent)
                                println("Login efetuado com sucesso!")
                                finish() // Fecha a atividade de login
                            } else {
                                // Mostrar mensagem de erro
                                val errorMessage = response.message
                                runOnUiThread {
                                    DialogUtils.showErrorPopup(
                                        context = this@LoginActivity,
                                        title = "Erro de Login",
                                        message = errorMessage
                                    )
                                }
                                println("Erro de login: $errorMessage")
                            }
                        }
                    }
                }
            } else {
                if (email.isEmpty()) emailEditText.error = "Preencha o campo de email!"
                if (password.isEmpty()) passwordEditText.error = "Preencha o campo de palavra-passe!"
            }
        }

        signupTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            println("Ir para a tela de registo")
            finish()
        }
    }

    private fun getSessionToken(): String? {
        val sharedPreferences = this.getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("SESSION_TOKEN", null)
    }

    private fun saveSessionToken(token: String) {
        val sharedPreferences = this.getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("SESSION_TOKEN", token)
        editor.apply() // Salva o token no SharedPreferences
    }
}

package com.example.datingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.API.ApiClient
import com.example.datingapp.API.Endpoints.LoginRequest
import com.example.datingapp.Utils.DataTypeUtils
import com.example.datingapp.Utils.DialogUtils
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
        val loadingProgressBar = findViewById<View>(R.id.loadingProgressBar)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty()) emailEditText.error = "Preencha o campo de email!"
            if (password.isEmpty()) passwordEditText.error = "Preencha o campo de palavra-passe!"
            if (email.isNotEmpty() and DataTypeUtils.isEmailValid(email).not()) emailEditText.error = "Email inválido!"

            if(email.isEmpty() || password.isEmpty() || DataTypeUtils.isEmailValid(email).not()) return@setOnClickListener

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(email, password)

                // Inicia o loading
                loginButton.text = ""
                loginButton.isEnabled = false
                loginButton.alpha = 0.5f
                loadingProgressBar.visibility = View.VISIBLE

                // Fazendo a requisição de login com Retrofit
                CoroutineScope(Dispatchers.Main).launch {
                    ApiClient.login(loginRequest) { response, error ->
                        // Para o loading independentemente do resultado
                        loginButton.text = "Entrar"
                        loginButton.isEnabled = true
                        loginButton.alpha = 1.0f
                        loadingProgressBar.visibility = View.GONE

                        if (error != null) {
                            println("Erro: $error")
                            DialogUtils.showErrorPopup(
                                context = this@LoginActivity,
                                title = "Erro de Login",
                                message = error
                            )
                        } else {
                            if (response != null && response.success) {
                                println("Login bem-sucedido. Token: ${response.token}")
                                saveSessionToken(response.token)
                                val intent = Intent(this@LoginActivity, HomepageActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                println("Erro de autenticação: ${response?.message}")
                                DialogUtils.showErrorPopup(
                                    context = this@LoginActivity,
                                    title = "Erro de Login",
                                    message = response?.message ?: "Erro desconhecido"
                                )
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

package com.example.datingapp.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.API.ApiClient
import com.example.datingapp.API.Endpoints.LoginRequest
import com.example.datingapp.R
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

        // Verifica se já existe um token de sessão salvo
        val token = getSessionToken()

        if (token != null) {
            // Se o utilizador já estiver autenticado, redireciona-o para a página de perfil
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            println("Token encontrado: $token")
            finish()
        // Fecha a LoginActivity para que o utilizador não possa voltar a ela
        }

        // Referências aos elementos do layout
        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val loginButton = findViewById<MaterialButton>(R.id.loginButton)
        val signupTextView = findViewById<TextView>(R.id.signupTextView)
        val loadingProgressBar = findViewById<View>(R.id.loadingProgressBar)

        // Configura o evento de clique no botão de login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validações dos campos de email e senha
            if (email.isEmpty()) emailEditText.error = "Preencha o campo de email!"
            if (password.isEmpty()) passwordEditText.error = "Preencha o campo de palavra-passe!"
            if (email.isNotEmpty() && !DataTypeUtils.isEmailValid(email)) emailEditText.error = "Email inválido!"

            // Se algum campo estiver incorreto, para a execução aqui
            if (email.isEmpty() || password.isEmpty() || !DataTypeUtils.isEmailValid(email)) return@setOnClickListener

            // Criar um objeto de requisição com o email e a senha inseridos
            val loginRequest = LoginRequest(email, password)

            // Inicia a animação de loading no botão de login
            loginButton.text = ""
            loginButton.isEnabled = false
            loginButton.alpha = 0.5f
            loadingProgressBar.visibility = View.VISIBLE

            // Faz a requisição de login usando corrotinas para chamadas assíncronas
            CoroutineScope(Dispatchers.Main).launch {
                ApiClient.login(loginRequest) { response, error ->
                    // Para o loading independentemente do resultado
                    loginButton.text = "Entrar"
                    loginButton.isEnabled = true
                    loginButton.alpha = 1.0f
                    loadingProgressBar.visibility = View.GONE

                    if (error != null) {
                        // Caso haja um erro na requisição, exibe um popup com a mensagem de erro
                        println("Erro: $error")
                        DialogUtils.showErrorPopup(
                            context = this@LoginActivity,
                            title = "Erro de Login",
                            message = error
                        )
                    } else {
                        if (response != null && response.success) {
                            // Se o login for bem-sucedido, salva o token de sessão
                            println("Login bem-sucedido. Token: ${response.token}")
                            saveSessionToken(response.token)

                            // Redireciona o utilizador para a página de perfil
                            val intent = Intent(this@LoginActivity, ProfilePage::class.java)
                            startActivity(intent)
                            finish()
                        // Fecha a LoginActivity para que o utilizador não possa voltar ao login
                        } else {
                            // Se houver erro de autenticação, exibe uma mensagem
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
        }

        // Configura o evento de clique no botão de "Criar conta"
        signupTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            println("Ir para a tela de registo")
            finish() // Fecha a LoginActivity para evitar que o utilizador volte ao login
        }
    }

    /**
     * Obtém o token de sessão salvo no SharedPreferences
     * Se não houver um token salvo, retorna null
     */
    private fun getSessionToken(): String? {
        val sharedPreferences = this.getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("SESSION_TOKEN", null)
    }

    /**
     * Salva o token de sessão no SharedPreferences para autenticação persistente
     */
    private fun saveSessionToken(token: String) {
        val sharedPreferences = this.getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("SESSION_TOKEN", token)
        editor.apply() // Salva o token no SharedPreferences
    }
}

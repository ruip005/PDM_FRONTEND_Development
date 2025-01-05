package com.example.datingapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.API.ApiRequest
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Deprecated_SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<TextInputEditText>(R.id.confirmPasswordEditText)
        val signUpButton = findViewById<MaterialButton>(R.id.signUpButton)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val response = withContext(Dispatchers.IO) {
                                val body = JSONObject().apply {
                                    put("email", email)
                                    put("password", password)
                                }
                                //val apiUrl = getString(R.string.apiSignUp)
                                val postRequest = ApiRequest("POST", "apiUrl", null, body)
                                postRequest.execute()
                            }

                            val jsonResponse = JSONObject(response)
                            val success = jsonResponse.optBoolean("success", false)

                            if (success) {
                                Toast.makeText(
                                    this@Deprecated_SignUpActivity,
                                    "Registro bem-sucedido! Faça login.",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish() // Voltar para a página de login
                            } else {
                                val errorMessage = jsonResponse.optString("message", "Erro ao registrar.")
                                DialogUtils.showErrorPopup(
                                    context = this@Deprecated_SignUpActivity,
                                    title = "Erro no Registro",
                                    message = errorMessage
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            DialogUtils.showErrorPopup(
                                context = this@Deprecated_SignUpActivity,
                                title = "Erro no Registro",
                                message = "Ocorreu um erro. Tente novamente."
                            )
                        }
                    }
                } else {
                    confirmPasswordEditText.error = "As senhas não coincidem."
                }
            } else {
                if (email.isEmpty()) emailEditText.error = "Preencha o campo de email!"
                if (password.isEmpty()) passwordEditText.error = "Preencha o campo de senha!"
                if (confirmPassword.isEmpty()) confirmPasswordEditText.error = "Confirme sua senha!"
            }
        }
    }
}

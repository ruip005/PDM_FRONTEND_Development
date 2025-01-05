package com.example.datingapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.API.Deprecated_ApiService
import org.json.JSONObject
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        // Referência ao AutoCompleteTextView
        val genderEditText = findViewById<AutoCompleteTextView>(R.id.genderAutoCompleteTextView)

        // Valores do menu suspenso
        val genderOptions = resources.getStringArray(R.array.gender_options)

        // Adaptador
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genderOptions)

        // Configurar o adaptador no AutoCompleteTextView
        genderEditText.setAdapter(adapter)

        // Forçar abertura do menu ao clicar (para teste)
        genderEditText.setOnClickListener {
            genderEditText.showDropDown()
        }

        val loginText = findViewById<TextView>(R.id.backToLoginTextView)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        };

        val registerButton = findViewById<TextView>(R.id.registerButton)

        val nameEditText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.nameEditText)
        val emailEditText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.passwordEditText)
        val birthDateEditText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.dateOfBirthEditText)

        // calendarii
        // Configurar o clique no campo de texto
        birthDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            // Abrir o DatePickerDialog
            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Atualizar o campo com a data selecionada
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    birthDateEditText.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePicker.show()
        }

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val birthDate = birthDateEditText.text.toString()
            val gender = genderEditText.text.toString()

            if (DataTypeUtils.isNameValid(name) && DataTypeUtils.isEmailValid(email) && DataTypeUtils.isPasswordValid(
                    password
                ) && DataTypeUtils.isDateCalendarValid(
                    DataTypeUtils.isAnAdult(birthDate).toString()
                )
                && gender.isNotEmpty()
            ) {
                // Fazer a requisição de registro
                val apiUrl = getString(R.string.APIRegister)

                // Criar o corpo da requisição em JSON
                val body = JSONObject().apply {
                    put("name", name)
                    put("email", email)
                    put("password", password)
                    put("gender", gender)
                    put("birthDate", birthDate)
                }.toString()
                // Headers (caso necessário, podes adicionar mais)
                val headers = mapOf(
                    "Content-Type" to "application/json"
                )
                Deprecated_ApiService.post(apiUrl, headers, body) { response, error ->
                    if (error != null) {
                        println(error)
                        runOnUiThread {
                            DialogUtils.showErrorPopup(
                                context = this@RegisterActivity,
                                title = "Erro de Registro",
                                message = "Não foi possível registrar o usuário. Por favor, tente novamente."
                            )
                        }
                    } else {
                        println(response)
                        runOnUiThread {
                            DialogUtils.showSuccessPopup(
                                context = this@RegisterActivity,
                                title = "Registo Concluído",
                                message = "Utilizador registado com sucesso."
                            )
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }

            } else {
                if (!DataTypeUtils.isNameValid(name)) {
                    nameEditText.error = "Nome inválido"
                }
                if (!DataTypeUtils.isEmailValid(email)) {
                    emailEditText.error = "Email inválido"
                }
                if (!DataTypeUtils.isPasswordValid(password)) {
                    passwordEditText.error = "Palavra-passe inválida"
                }
                if (!DataTypeUtils.isAnAdult(birthDate)) {
                    birthDateEditText.error = "Precisa ser maior de idade"
                }
                if (gender.isEmpty()) {
                    genderEditText.error = "Gênero inválido"
                }
                if (!DataTypeUtils.isDateCalendarValid(birthDate)) {
                    birthDateEditText.error = "Data de nascimento inválida"
                } /* else {
                DialogUtils.showErrorPopup(
                    context = this@RegisterActivity,
                    title = "Erro de Validação",
                    message = "Por favor, preencha todos os campos corretamente."
                )
            }*/
            }
        }
    }
}

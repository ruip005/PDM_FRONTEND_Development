package com.example.datingapp.Activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.API.ApiClient
import com.example.datingapp.API.Endpoints.RegisterRequest
import com.example.datingapp.R
import com.example.datingapp.Utils.DataTypeUtils
import com.example.datingapp.Utils.DialogUtils
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val genderEditText = findViewById<AutoCompleteTextView>(R.id.genderAutoCompleteTextView)
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genderOptions)
        genderEditText.setAdapter(adapter)

        genderEditText.setOnClickListener { genderEditText.showDropDown() }

        val loginText = findViewById<TextView>(R.id.backToLoginTextView)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val registerButton = findViewById<TextView>(R.id.registerButton)
        val nameEditText = findViewById<TextInputEditText>(R.id.nameEditText)
        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<TextInputEditText>(R.id.confirmPasswordEditText)
        val birthDateEditText = findViewById<TextInputEditText>(R.id.dateOfBirthEditText)

        birthDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
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
            val confirmPassword = confirmPasswordEditText.text.toString()
            val birthDate = birthDateEditText.text.toString()
            val gender = genderEditText.text.toString()

            if (DataTypeUtils.isNameValid(name) &&
                DataTypeUtils.isEmailValid(email) &&
                DataTypeUtils.isPasswordValid(password) &&
                DataTypeUtils.isDateCalendarValid(birthDate) &&
                DataTypeUtils.isAnAdult(birthDate.toString()) &&
                gender.isNotEmpty() && password == confirmPassword

            ) {
                val registerRequest = RegisterRequest(name, email, password, gender, birthDate)

                ApiClient.register(registerRequest) { response, error ->
                    if (error != null) {
                        runOnUiThread {
                            DialogUtils.showErrorPopup(
                                context = this@RegisterActivity,
                                title = "Erro de Registro",
                                message = error
                            )
                        }
                    } else {
                        //runOnUiThread {
                            DialogUtils.showSuccessPopup(
                                context = this@RegisterActivity,
                                title = "Registo Concluído",
                                message = "Utilizador registado com sucesso."
                            )
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        //}
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
                }
                if (password != confirmPassword) {
                    confirmPasswordEditText.error = "As palavras-passe não coincidem"
                    passwordEditText.error = "As palavras-passe não coincidem"
                }
            }
        }
    }
}

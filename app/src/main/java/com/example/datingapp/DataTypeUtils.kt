package com.example.datingapp

import java.util.Calendar

class DataTypeUtils {
    companion object {
        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPasswordValid(password: String): Boolean {
            return password.length >= 6
        }

        fun isNameValid(name: String): Boolean {
            // Nome deve ter pelo menos 3 caracteres e apenas letras com espaços e acentos
            return name.length >= 3 && name.matches(Regex("^[a-zA-ZÀ-ú ]+\$"))
        }

        fun isAnAdult(birthDate: String): Boolean {
            try {
                // Verifica se a pessoa é maior de idade
                val birthYear = birthDate.split("/")[2].toInt()
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                return currentYear - birthYear >= 18
            } catch (e: Exception) {
                return false
            }
        }

        fun isDateCalendarValid(date: String): Boolean {
            // Verifica se a data é válida
            try {
                val dateParts = date.split("/")
                val day = dateParts[0].toInt()
                val month = dateParts[1].toInt()
                val year = dateParts[2].toInt()
                val calendar = Calendar.getInstance()
                calendar.set(year, month - 1, day)
                return day == calendar.get(Calendar.DAY_OF_MONTH) && month == calendar.get(Calendar.MONTH) + 1 && year == calendar.get(
                    Calendar.YEAR
                )
            } catch (e: Exception) {
                return false
            }
        }
    }
}
package com.example.datingapp.Utils

import android.content.Context
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import org.json.JSONObject

object DataUtils {

    /**
     * Busca o JWT armazenado nas SharedPreferences, decodifica o token e retorna os dados do payload.
     *
     * @param context O contexto necessário para acessar SharedPreferences.
     * @return JSONObject contendo os dados do payload ou null se o token não existir ou for inválido.
     */

    fun parseJwt(context: Context): JSONObject? {
        // Acessa o token armazenado nas SharedPreferences
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("SESSION_TOKEN", null)

        // Verifica se o token existe
        if (token.isNullOrEmpty()) {
            return null
        }

        return try {
            // Divide o token em partes (header, payload, signature)
            val parts = token.split(".")
            if (parts.size != 3) {
                throw IllegalArgumentException("JWT inválido")
            }

            // O payload é a segunda parte do token
            val payload = parts[1]

            // Decodifica a parte do payload de Base64 para String
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes)

            // Converte a string JSON para JSONObject e retorna
            return JSONObject(decodedString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun parseJwt(token: String): JSONObject? {
        return try {
            // Divide o token em partes (header, payload, signature)
            val parts = token.split(".")
            if (parts.size != 3) {
                throw IllegalArgumentException("JWT inválido")
            }

            // O payload é a segunda parte do token
            val payload = parts[1]

            // Decodifica a parte do payload de Base64 para String
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes)

            // Converte a string JSON para JSONObject e retorna
            JSONObject(decodedString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

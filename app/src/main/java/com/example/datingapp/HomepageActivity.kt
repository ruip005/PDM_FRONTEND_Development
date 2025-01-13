package com.example.datingapp

import com.example.datingapp.*
import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.API.*
import com.example.datingapp.Utils.DataUtils
import com.example.datingapp.Utils.DialogUtils
import java.util.Calendar

class HomepageActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Exemplo de botão de like/dislike
        val likeButton = findViewById<Button>(R.id.likeButton)
        val logoutButton = findViewById<Button>(R.id.logoutbutton)

        logoutButton.setOnClickListener {
            logout()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        likeButton.setOnClickListener {
            // Lógica para dar like
        }

        //try {
        val profileBlock = findViewById<ProfileBlockView>(R.id.profileBlock)
        // JWT token leva sub (guid|id), name, email, birthday
        DataUtils.parseJwt(this)?.let { jwtPayload ->
            println("Dados do payload: $jwtPayload")
            val guid = jwtPayload.getString("sub")
            val name = jwtPayload.getString("name")
            val birthday = jwtPayload.getString("birthdate")
            val age = calculateAge(birthday)

            ApiClient.getUserPhoto(guid) { imageData, error ->
                if (error != null) {
                    DialogUtils.showErrorToast(this, error)
                    profileBlock.setProfileData(name, age, BitmapFactory.decodeResource(resources, R.drawable.ic_profile_placeholder))
                } else if (imageData != null) {
                    val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                    profileBlock.setProfileData(name, age, bitmap)
                } else {
                    DialogUtils.showErrorToast(this, "Erro ao carregar imagem")
                    profileBlock.setProfileData(name, age, BitmapFactory.decodeResource(resources, R.drawable.ic_profile_placeholder))
                }
            }
        }
    /*}
    catch (e: Exception) {
        DialogUtils.showErrorToast(this, "Erro ao carregar perfil")
    }*/
    }

    private fun logout() {
        val sharedPreferences = this.getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("SESSION_TOKEN")
        editor.apply()
    }
    private fun calculateAge(birthdate: String): Int {
        // Formato da data esperado: 2003-08-03T23:00:00.000Z
        val birthdateParts = birthdate.split("T")[0].split("-") // Separa a data no formato ISO 8601
        val year = birthdateParts[0].toInt()
        val month = birthdateParts[1].toInt() - 1 // Ajusta o mês para o índice do Calendar (0-based)
        val day = birthdateParts[2].toInt()

        // Cria o calendário com a data de nascimento
        val birthdateCalendar = Calendar.getInstance()
        birthdateCalendar.set(year, month, day)

        // Obter a data atual
        val todayCalendar = Calendar.getInstance()

        // Calcula a idade
        var age = todayCalendar.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR)

        // Ajusta a idade caso o aniversário ainda não tenha ocorrido este ano
        if (todayCalendar.get(Calendar.DAY_OF_YEAR) < birthdateCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }
}
// Improved HomepageActivity.kt
package com.example.datingapp

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.API.ApiClient
import com.example.datingapp.Utils.DataUtils
import com.example.datingapp.Utils.DialogUtils
import com.example.datingapp.ProfileBlockView
import java.util.Calendar

class HomepageActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val likeButton: Button = findViewById(R.id.likeButton)
        val logoutButton: Button = findViewById(R.id.logoutbutton)
        val profileBlock: ProfileBlockView = findViewById(R.id.profileBlock)

        setupLogoutButton(logoutButton)
        setupLikeButton(likeButton)
        loadUserProfile(profileBlock)
    }

    private fun setupLogoutButton(logoutButton: Button) {
        logoutButton.setOnClickListener {
            logout()
            navigateToLogin()
        }
    }

    private fun setupLikeButton(likeButton: Button) {
        likeButton.setOnClickListener {
            // Implement your like logic here
        }
    }

    private fun loadUserProfile(profileBlock: ProfileBlockView) {
        val jwtPayload = DataUtils.parseJwt(this)
        if (jwtPayload != null) {
            val guid = jwtPayload.getString("sub")
            val name = jwtPayload.getString("name")
            val birthday = jwtPayload.getString("birthdate")
            val age = calculateAge(birthday)

            ApiClient.getUserPhoto(guid) { imageData, error ->
                if (error != null) {
                    showErrorToast("Erro ao carregar imagem do usuário: $error")
                    setDefaultProfileData(profileBlock, name, age)
                } else if (imageData != null) {
                    val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                    profileBlock.setProfileData(name, age, bitmap)
                } else {
                    showErrorToast("Erro ao carregar imagem")
                    setDefaultProfileData(profileBlock, name, age)
                }
            }
        } else {
            showErrorToast("Erro ao carregar dados do usuário")
        }
    }

    private fun setDefaultProfileData(profileBlock: ProfileBlockView, name: String, age: Int) {
        val defaultImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_profile_placeholder)
        profileBlock.setProfileData(name, age, defaultImage)
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        sharedPreferences.edit().remove("SESSION_TOKEN").apply()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun calculateAge(birthdate: String): Int {
        try {
            val birthdateParts = birthdate.split("T")[0].split("-")
            val year = birthdateParts[0].toInt()
            val month = birthdateParts[1].toInt() - 1
            val day = birthdateParts[2].toInt()

            val birthdateCalendar = Calendar.getInstance().apply {
                set(year, month, day)
            }
            val todayCalendar = Calendar.getInstance()

            var age = todayCalendar.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR)
            if (todayCalendar.get(Calendar.DAY_OF_YEAR) < birthdateCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            return age
        } catch (e: Exception) {
            showErrorToast("Erro ao calcular idade: ${e.message}")
            return 0
        }
    }

    private fun showErrorToast(message: String) {
        DialogUtils.showErrorToast(this, message)
    }
}

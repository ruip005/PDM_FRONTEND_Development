package com.example.datingapp.Activities

import QRCodeGenerator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.API.ApiClient
import com.example.datingapp.ProfileBlockView
import com.example.datingapp.R
import com.example.datingapp.Rating
import com.example.datingapp.Utils.DataUtils
import com.example.datingapp.Utils.DialogUtils
import java.util.Calendar

class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_profile_page)

        val profileBlock: ProfileBlockView = findViewById(R.id.profileBlock)

        loadUserProfile(profileBlock)

        val searchBtn = findViewById<Button>(R.id.searchPeople)

        searchBtn.setOnClickListener {
            val intent = Intent(this, Rating::class.java)
            startActivity(intent)
        }

        //val openScanner = findViewById<ImageView>(R.id.scanQrCodeIcon)
        //openScanner.setOnClickListener {
        //    val intent = Intent(this, scanner_block::class.java)
        //    startActivity(intent)
        //}
    }

    private fun loadUserProfile(profileBlock: ProfileBlockView) {
        val jwtPayload = DataUtils.parseJwt(this)
        if (jwtPayload != null) {
            val guid = jwtPayload.getString("sub")
            val name = jwtPayload.getString("name")
            val birthday = jwtPayload.getString("birthdate")
            val age = calculateAge(birthday)
            val fileName = "user_qrcode.png"

            // Gerar e salvar QR Code
            try {
                val userData = "https://api.triumphmc.tech/v1/user/view/$guid"
                if (QRCodeGenerator.hasQRCode(this, fileName).not()) QRCodeGenerator.generateAndSaveQRCode(this, userData, fileName)
                // set QR Code qrCodeBlock

                val qrCodeBlock = findViewById<ImageView>(R.id.qrCodeImageView)
                qrCodeBlock.setImageBitmap(BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + fileName))
            }
            catch (e: Exception) {
                showErrorToast("Erro ao gerar QR Code: ${e.message}")
            }

            ApiClient.getUserPhoto(guid) { imageData, error ->
                if (error != null) {
                    showErrorToast("Erro ao carregar imagem do utilizador")
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
        val defaultImage: Bitmap = BitmapFactory.decodeResource(resources,
            R.drawable.ic_profile_placeholder
        )
        profileBlock.setProfileData(name, age, defaultImage)
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
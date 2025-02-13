package com.example.datingapp.Activities

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.datingapp.API.ApiClient
import com.example.datingapp.Fragments.QrScannerFragment
import com.example.datingapp.ProfileBlockView
import com.example.datingapp.R
import com.example.datingapp.Utils.DataUtils
import com.example.datingapp.Utils.DialogUtils
import java.util.Calendar

class ProfilePage : AppCompatActivity() {
//    private lateinit var bluetoothManager: BluetoothManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita o modo Edge-to-Edge para otimizar o layout na tela inteira
        setContentView(R.layout.activity_my_profile_page)


    val profileBlock: ProfileBlockView = findViewById(R.id.profileBlock)

    loadUserProfile(profileBlock)
        // Carregar o fragmento do scanner
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val qrScannerFragment = QrScannerFragment()
        fragmentTransaction.replace(R.id.fragment_container, qrScannerFragment)
        fragmentTransaction.commit()

        //  Configuração dos botões do menu de navegação
        val menuProfile = findViewById<LinearLayout>(R.id.menuMeuPerfil)
        val menuRating = findViewById<LinearLayout>(R.id.menuRating)
        val menuChat = findViewById<LinearLayout>(R.id.menuChat)

        // Se o utilizador já está na `ProfilePage`, evita recriar a atividade
        menuProfile.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT // Reutiliza a instância se já estiver aberta
            startActivity(intent)
        }

        // Navega para a `ProfileActivity` (tela de rating)
        menuRating.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        // Navega para a `ChatActivity`
        menuChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        val logoutBtn = findViewById<Button>(R.id.btnLogout)

    logoutBtn.setOnClickListener {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        sharedPreferences.edit().remove("SESSION_TOKEN").apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    val btnBlue = findViewById<ImageView>(R.id.btnBluetooth)

    btnBlue.setOnClickListener {
        val intent = Intent(this, BluetoothDevicesActivity::class.java)
        startActivity(intent)
    }
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
            showErrorToast("Erro ao carregar dados do usuÃ¡rio")
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
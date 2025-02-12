package com.example.datingapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.datingapp.Fragments.QrScannerFragment
import com.example.datingapp.R

class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita o modo Edge-to-Edge para otimizar o layout na tela inteira
        setContentView(R.layout.activity_my_profile_page)

        // Carrega o fragmento do scanner QR dentro do `fragment_container`
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
    }
}

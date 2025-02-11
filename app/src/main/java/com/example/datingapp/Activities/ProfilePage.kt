package com.example.datingapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_profile_page)

        // Carregar o fragmento do scanner
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val qrScannerFragment = QrScannerFragment()
        fragmentTransaction.replace(R.id.fragment_container, qrScannerFragment)
        fragmentTransaction.commit()

        val menuProfile = findViewById<LinearLayout>(R.id.menuMeuPerfil)
        val menuRating = findViewById<LinearLayout>(R.id.menuRating)
        val menuChat = findViewById<LinearLayout>(R.id.menuChat)

        menuProfile.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }

        menuRating.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        menuChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }
    }
}
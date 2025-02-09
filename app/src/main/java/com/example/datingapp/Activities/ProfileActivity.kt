package com.example.datingapp.Activities

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.datingapp.API.ApiClient
import com.example.datingapp.API.Endpoints.GetProfileRequest
import com.example.datingapp.R
import com.example.datingapp.adapters.AdditionalPhotosAdapter
import com.example.datingapp.databinding.ActivityProfileBinding
import com.example.datingapp.Classes.*
import com.example.datingapp.Utils.DialogUtils
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val sharedPreferences by lazy {
        getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userGuid = getUserGuid() // Obtém ou gera um novo GUID

        if (userGuid == null) {
            fetchNewUserGuid() // Se não existir, obtém um novo GUID
        } else {
            loadUserProfile(userGuid) // Se já existir, carrega o perfil
        }
    }

    private fun getUserGuid(): String? {
        return sharedPreferences.getString("USER_GUID", null)
    }

    private fun saveUserGuid(userGuid: String) {
        sharedPreferences.edit().putString("USER_GUID", userGuid).apply()
    }

    private fun fetchNewUserGuid() {
        ApiClient.getNewUserGuid(this) { newGuid, error ->
            if (error != null) {
                DialogUtils.showErrorPopup(this, "Erro ao obter GUID", error)
            } else if (newGuid != null) {
                saveUserGuid(newGuid) // Guarda o novo GUID
                loadUserProfile(newGuid) // Carrega o perfil com o novo GUID
            }
        }
    }

    private fun loadUserProfile(userGuid: String) {
        val request = GetProfileRequest(isFull = true, morePhotos = true)

        ApiClient.getUser(this, userGuid, request) { response, error ->
            if (error != null) {
                DialogUtils.showErrorPopup(this, "Erro ao buscar perfil", error)
            } else {
                response?.user?.let { user ->
                    displayUserProfile(user)
                }
            }
        }
    }

    private fun decisionUser(userGuid: String, decision: DecisionType) {
        ApiClient.decisionUser(this,
            userGuid,
            decision.toString()
        ) { error ->
            if (error != null) {
                DialogUtils.showErrorPopup(this, "Erro ao passar utilizador", error)
            } else {
                DialogUtils.showSuccessToast(this, if (decision == DecisionType.smash) "Smash!" else "Pass!")
            }
        }
    }

    private fun displayUserProfile(user: User) {
        binding.profileName.text = user.name

        Glide.with(this)
            .load(user.profilePicture?.binary)
            .into(binding.profileImage)

        val profileDetail = user.profileDetail
        profileDetail?.let {
            binding.profileHeight.text = "Altura: ${it.height} cm"
            binding.profileQualifications.text = "Qualificações: ${it.qualifications}"
            binding.profileSign.text = "Signo: ${it.sign}"
            binding.profileChildren.text = "Crianças: ${it.wantChilds}"
            binding.profileCommunicationStyle.text = "Estilo de comunicação: ${it.styleOfCommunication}"
            binding.profileLoveType.text = "Tipo de amor: ${it.typeOfLove}"
            binding.profileLookingFor.text = "Procurando: ${it.lookingFor}"
            binding.profileAnimals.text = "Animais: ${it.animals}"
            binding.profileSmoke.text = "Fuma: ${it.smoking}"
            binding.profileDrink.text = "Bebe: ${it.drinking}"
            binding.profileGym.text = "Ginásio: ${it.gym}"
            binding.profileSexualOrientation.text = "Orientação sexual: ${it.sexualOrientation}"
            binding.profileSchool.text = "Escola: ${it.school}"
        }

        val likeBtn = findViewById<Button>(R.id.dislikeButton)
        likeBtn.setOnClickListener {
            decisionUser(user.guid.toString(), DecisionType.smash)
        }

        val passBtn = findViewById<Button>(R.id.dislikeButton)
        passBtn.setOnClickListener {
            decisionUser(user.guid.toString(), DecisionType.pass)
        }

        user.photos?.let { photos ->
            val photoUrls = photos.map { it.photoBinary.toString() }
            val adapter = AdditionalPhotosAdapter(photoUrls)
            binding.additionalPhotosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.additionalPhotosRecyclerView.adapter = adapter
        }
    }
}


enum class DecisionType {
    smash, pass
}
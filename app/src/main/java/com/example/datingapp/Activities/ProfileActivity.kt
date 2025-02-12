package com.example.datingapp.Activities

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.datingapp.API.ApiClient
import com.example.datingapp.API.ApiClient.decisionUser
import com.example.datingapp.API.Endpoints.GetProfileRequest
import com.example.datingapp.R
import com.example.datingapp.adapters.AdditionalPhotosAdapter
import com.example.datingapp.databinding.ActivityRatingBinding
import com.example.datingapp.Utils.DialogUtils
import com.example.datingapp.Classes.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatingBinding
    private val sharedPreferences by lazy {
        getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verifica se há um userGuid disponível
        val userGuid = getUserGuid()

        if (userGuid == null) {
            fetchNewUserGuid() // Se não existir, busca um novo GUID
        } else {
            loadUserProfile(userGuid) // Se já existir, carrega o perfil
        }

        // Configura os botões de decisão
        setupDecisionButtons()
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
                saveUserGuid(newGuid) // Salva o novo GUID
                loadUserProfile(newGuid) // Carrega o perfil com o novo GUID
            }
        }
    }

    private fun loadUserProfile(userGuid: String) {
        val request = GetProfileRequest(isFull = true, morePhotos = true)
        try {
            print("userGuid: $userGuid")
            ApiClient.getUser(this, userGuid, request) { response, error ->
                if (error != null) {
                    DialogUtils.showErrorPopup(this, "Erro ao buscar perfil", error)
                } else {
                    response?.user?.let { user ->
                        displayUserProfile(user)
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao buscar perfil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayUserProfile(user: User) {
        binding.profileName.text = user.name

        // Carrega a imagem do perfil
        Glide.with(this)
            .load(user.profilePicture?.binary)
            .into(binding.profileImage)

        // Exibe os detalhes do perfil
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

        // Configura o RecyclerView para fotos adicionais
        user.photos?.let { photos ->
            val photoUrls = photos.map { it.photoBinary.toString() }
            val adapter = AdditionalPhotosAdapter(photoUrls)
            binding.additionalPhotosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.additionalPhotosRecyclerView.adapter = adapter
        }
    }

    private fun setupDecisionButtons() {
        binding.dislikeButton.setOnClickListener {
            val userGuid = getUserGuid()
            if (userGuid != null) {
                decisionUser(userGuid, DecisionType.pass)
            } else {
                Toast.makeText(this, "Erro: GUID do user não encontrado.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.likeButton.setOnClickListener {
            val userGuid = getUserGuid()
            if (userGuid != null) {
                decisionUser(userGuid, DecisionType.smash)
            } else {
                Toast.makeText(this, "Erro: GUID do user não encontrado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun decisionUser(userGuid: String, decision: DecisionType) {
        ApiClient.decisionUser(this, userGuid, decision.toString()) { error ->
            if (error != null) {
                DialogUtils.showErrorPopup(this, "Erro ao enviar decisão", error)
            } else {
                Toast.makeText(this, if (decision == DecisionType.smash) "Smash!" else "Pass!", Toast.LENGTH_SHORT).show()
                fetchNewUserGuid() // Busca um novo perfil após a decisão
            }
        }
    }
}

enum class DecisionType {
    smash, pass
}
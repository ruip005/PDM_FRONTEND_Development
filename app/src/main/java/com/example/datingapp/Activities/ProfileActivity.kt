package com.example.datingapp.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.datingapp.API.ApiClient
import com.example.datingapp.API.Endpoints.GetProfileRequest
import com.example.datingapp.R
import com.example.datingapp.Utils.DialogUtils
import com.example.datingapp.adapters.AdditionalPhotosAdapter
import com.example.datingapp.Classes.*
import com.example.datingapp.databinding.ActivityRatingBinding

class ProfileActivity : AppCompatActivity() {

    //  Inicializa a View Binding para acessar os elementos do layout
    private lateinit var binding: ActivityRatingBinding

    //  SharedPreferences para salvar e recuperar o GUID do utilizador
    private val sharedPreferences by lazy {
        getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração do menu de navegação (Perfil, Ratings e Chat)
        val menuProfile = findViewById<LinearLayout>(R.id.menuMeuPerfil)
        val menuRating = findViewById<LinearLayout>(R.id.menuRating)
        val menuChat = findViewById<LinearLayout>(R.id.menuChat)

        // Navegação entre as atividades sem criar novas instâncias desnecessárias
        menuProfile.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        menuRating.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        menuChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        // Obtém o userGuid salvo ou busca um novo se não existir
        val userGuid = getUserGuid()
        if (userGuid == null) {
            fetchNewUserGuid()
        } else {
            loadUserProfile(userGuid)
        }

        // Configura os botões de "Like" e "Dislike"
        setupDecisionButtons()
    }

    /**
     * Obtém o GUID do utilizador armazenado no SharedPreferences
     * Se não houver um GUID salvo, retorna null.
     */
    private fun getUserGuid(): String? {
        return sharedPreferences.getString("USER_GUID", null)
    }

    /**
     * Salva o GUID do utilizador no SharedPreferences para reutilização.
     */
    private fun saveUserGuid(userGuid: String) {
        sharedPreferences.edit().putString("USER_GUID", userGuid).apply()
    }

    /**
     *  Faz uma requisição para obter um novo GUID do utilizador se não existir um armazenado.
     */
    private fun fetchNewUserGuid() {
        ApiClient.getNewUserGuid(this) { newGuid, error ->
            if (error != null) {
                DialogUtils.showErrorPopup(this, "Erro ao obter GUID", error)
            } else if (newGuid != null) {
                saveUserGuid(newGuid) //  Salva o novo GUID
                loadUserProfile(newGuid) // Carrega o perfil correspondente ao novo GUID
            }
        }
    }

    /**
     * Carrega o perfil do utilizador a partir do API usando o GUID fornecido.
     */
    private fun loadUserProfile(userGuid: String) {
        val request = GetProfileRequest(isFull = true, morePhotos = true)
        try {
            ApiClient.getUser(this, userGuid, request) { response, error ->
                if (error != null) {
                    DialogUtils.showErrorPopup(this, "Erro ao buscar perfil", error)
                } else {
                    response?.user?.let { user ->
                        displayUserProfile(user) //  Exibe os detalhes do utilizador na UI
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao buscar perfil", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Atualiza a interface do utilizador com as informações do perfil carregado.
     */
    private fun displayUserProfile(user: User) {
        binding.profileName.text = user.name

        //  Carrega a imagem do perfil usando Glide
        Glide.with(this)
            .load(user.profilePicture?.binary)
            .into(binding.profileImage)

        // Exibe os detalhes do perfil do utilizador
        user.profileDetail?.let {
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

        // Configuração do RecyclerView para fotos adicionais
        user.photos?.let { photos ->
            val photoUrls = photos.map { it.photoBinary.toString() }
            val adapter = AdditionalPhotosAdapter(photoUrls)
            binding.additionalPhotosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.additionalPhotosRecyclerView.adapter = adapter
        }
    }

    /**
     * Configura os botões de "Like" e "Dislike" para interagir com os perfis.
     */
    private fun setupDecisionButtons() {
        binding.dislikeButton.setOnClickListener {
            val userGuid = getUserGuid()
            if (userGuid != null) {
                decisionUser(userGuid, DecisionType.pass)
            } else {
                Toast.makeText(this, "Erro: GUID do utilizador não encontrado.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.likeButton.setOnClickListener {
            val userGuid = getUserGuid()
            if (userGuid != null) {
                decisionUser(userGuid, DecisionType.smash)
            } else {
                Toast.makeText(this, "Erro: GUID do utilizador não encontrado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Envia a decisão do utilizador (Like ou Dislike) para a API.
     */
    private fun decisionUser(userGuid: String, decision: DecisionType) {
        ApiClient.decisionUser(this, userGuid, decision.toString()) { error ->
            if (error != null) {
                DialogUtils.showErrorPopup(this, "Erro ao enviar decisão", error)
            } else {
                Toast.makeText(this, if (decision == DecisionType.smash) "Smash!" else "Pass!", Toast.LENGTH_SHORT).show()
                fetchNewUserGuid() // 🔹 Obtém um novo perfil após a decisão
            }
        }
    }

    /**
     * Comportamento personalizado para o botão "Voltar".
     * Ao pressionar "Voltar", redireciona para a `ProfilePage`.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ProfilePage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
    }
}

/**
 * Enum para representar as possíveis decisões do utilizador (Like ou Dislike).
 */
enum class DecisionType {
    smash, pass
}

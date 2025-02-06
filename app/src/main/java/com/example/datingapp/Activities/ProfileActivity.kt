package com.example.datingapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.*
import com.example.datingapp.Classes.*
import com.example.datingapp.adapters.AdditionalPhotosAdapter
import com.example.datingapp.databinding.ActivityProfileBinding
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Exemplo de dados de utilizador
        val user = User(
            id = 1,
            guid = UUID.randomUUID(),
            name = "João Silva",
            email = "joao.silva@example.com",
            password = "password123",
            gender = "M",
            birthdate = Date(),
            phone = 123456789,
            isActive = true,
            createdOn = Date(),
            updatedOn = Date(),
            profilePicture = UserProfilePicture(
                id = 1,
                binary = byteArrayOf() // Dados binários da foto de perfil
            ),
            photos = listOf(
                UserPhotos(
                    id = 1,
                    userId = 1,
                    photoBinary = byteArrayOf() // Dados binários da foto
                )
            ),
            profileDetail = ProfileDetail(
                id = 1,
                height = 180,
                qualifications = "Licenciatura",
                sign = "Leão",
                wantChilds = "Quero ter filhos",
                styleOfCommunication = "Mensagem",
                typeOfLove = "Tempo juntos",
                lookingFor = "Relação séria",
                animals = "Cão",
                smoking = "Não fumo",
                drinking = "De vez em quando",
                gym = "Vou ao ginásio",
                sexualOrientation = "Heterossexual",
                school = "Universidade de Lisboa"
            )
        )

        // Exibir os dados do utilizador
        displayUserProfile(user)
    }

    private fun displayUserProfile(user: User) {
        binding.profileName.text = user.name
        // Carregar a imagem de perfil usando Glide
        Glide.with(this)
            .load(user.profilePicture?.binary)
            .into(binding.profileImage)

        val profileDetail = user.profileDetail
        if (profileDetail != null) {
            binding.profileHeight.text = "Altura: ${profileDetail.height} cm"
            binding.profileQualifications.text = "Qualificações: ${profileDetail.qualifications}"
            //binding.profileSign.text = "Signo: ${profileDetail.sign}"
            //binding.profileWantChilds.text = "Desejo de ter filhos: ${profileDetail.wantChilds}"
            //binding.profileStyleOfCommunication.text = "Estilo de comunicação: ${profileDetail.styleOfCommunication}"
            //binding.profileTypeOfLove.text = "Tipo de amor: ${profileDetail.typeOfLove}"
            //binding.profileLookingFor.text = "À procura de: ${profileDetail.lookingFor}"
            //binding.profileAnimals.text = "Animais: ${profileDetail.animals}"
            //binding.profileSmoking.text = "Fumo: ${profileDetail.smoking}"
            //binding.profileDrinking.text = "Bebida: ${profileDetail.drinking}"
            //binding.profileGym.text = "Ginásio: ${profileDetail.gym}"
            //binding.profileSexualOrientation.text = "Orientação sexual: ${profileDetail.sexualOrientation}"
            //binding.profileSchool.text = "Escola: ${profileDetail.school}"
        }

        // Exibir fotos adicionais
        user.photos?.let { photos ->
            val photoUrls = photos.map { it.photoBinary.toString() } // Converta os dados binários para URLs
            val adapter = AdditionalPhotosAdapter(photoUrls)
            binding.additionalPhotosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.additionalPhotosRecyclerView.adapter = adapter
        }
    }
}
package com.example.datingapp.API.Endpoints

import com.example.datingapp.API.Endpoints.*
import com.example.datingapp.Classes.*

data class GetProfileResponse(
    val success: Boolean,
    val message: String,
    val user: User?,
    val profileDetail: ProfileDetail?,
    val userPhotos: List<UserPhotos>?,
    val userPhoto: UserProfilePicture?
)

/*
package com.example.datingapp.API.Endpoints

data class GetProfileResponse(
    val success: Boolean,
    val message: String? = null,
    val user: UserDetails? = null
)

data class UserDetails(
    val guid: String,
    val name: String,
    val email: String,
    val gender: String,
    val birthdate: String,
    val phone: Long,
    val isActive: Boolean,
    val createdOn: String,
    val updatedOn: String,
    val createdAt: String,
    val updatedAt: String,
    val profileDetail: ProfileDetail? = null,
    val userPhoto: UserPhoto? = null,
    val userPhotos: List<UserPhoto>? = null
)

data class ProfileDetail(
    val height: String?,
    val qualifications: String?,
    val sign: String?,
    val wantChilds: String?,
    val styleOfCommunication: String?,
    val typeOfLove: String?,
    val lookingFor: String?,
    val animals: String?,
    val smoking: String?,
    val drinking: String?,
    val gym: String?,
    val sexualOrientation: String?,
    val school: String?,
    val createdAt: String,
    val updatedAt: String
)

data class UserPhoto(
    val binary: BinaryData,
    val createdAt: String,
    val updatedAt: String
)

data class BinaryData(
    val type: String,
    val data: List<Int>
)
*/
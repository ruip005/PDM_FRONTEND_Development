package com.example.datingapp.API

import com.example.datingapp.API.Endpoints.ChangePasswordRequest
import com.example.datingapp.API.Endpoints.ChangePasswordResponse
import com.example.datingapp.API.Endpoints.GetNewUserGuidResponse
import com.example.datingapp.API.Endpoints.GetProfileRequest
import com.example.datingapp.API.Endpoints.GetProfileResponse
import com.example.datingapp.API.Endpoints.LoginRequest
import com.example.datingapp.API.Endpoints.LoginResponse
import com.example.datingapp.API.Endpoints.RegisterRequest
import com.example.datingapp.API.Endpoints.RegisterResponse
import com.example.datingapp.API.Endpoints.UpdateUserRequest
import com.example.datingapp.API.Endpoints.UpdateUserResponse
import com.example.datingapp.API.Endpoints.UploadProfileResponse
import com.example.datingapp.Database.Message
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {

    // Função GET com cabeçalhos dinâmicos
    @GET
    fun get(@Url url: String, @HeaderMap headers: Map<String, String>): Call<String>

    // Função POST para login (com corpo)
    @POST("v1/auth/login")  // Exemplo de especificação da URL, pode ser ajustado conforme necessário
    fun login(@Body body: LoginRequest): Call<LoginResponse>

    @POST("v1/auth/register")  // Exemplo de especificação da URL, pode ser ajustado conforme necessário
    fun register(@Body body: RegisterRequest): Call<RegisterResponse>

    @POST("v1/auth/newpwd")
    fun changePassword(
        @Body body: ChangePasswordRequest,
        @Header("Authorization") token: String
    ): Call<ChangePasswordResponse>

    @PUT("v1/auth/update")
    fun updateUser(
        @Body body: UpdateUserRequest,
        @Header("Authorization") token: String
    ): Call<UpdateUserResponse>

    @GET("v1/user/u/{id}")
    fun getProfile(
        @Path("id") userId: String,
        @Body body: GetProfileRequest,
    ): Call<GetProfileResponse>

    @GET("v1/user/u/{id}")
    fun getUser(
        @Path("id") userId: String,
        @Body body: GetProfileRequest
    ): Call<GetProfileResponse>

    @Multipart
    @POST("v1/user/upload")
    fun uploadProfilePicture(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part?
    ): Call<UploadProfileResponse>

    @GET("v1/imagestest/photo/{userGuid}")
    fun getUserPhoto(
        @Path("userGuid") userGuid: String
    ): Call<ResponseBody>

    @GET("v1/user/search")
    fun getNewUserGuid(
        @Header("Authorization") token: String
    ): Call<GetNewUserGuidResponse>


    @POST("v1/user/decision")
    fun decisionUser(
        @Header("Authorization") token: String,
        @Body body: String
    ): Call<String>

    // Função POST para dados genéricos (com cabeçalhos e corpo)
    @POST
    fun postData(@Url url: String, @HeaderMap headers: Map<String, String>, @Body body: String): Call<String>

    // Função PUT com cabeçalhos dinâmicos e corpo
    @PUT
    fun put(@Url url: String, @HeaderMap headers: Map<String, String>, @Body body: String): Call<String>
}

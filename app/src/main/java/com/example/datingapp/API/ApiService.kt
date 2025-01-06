package com.example.datingapp.API

import com.example.datingapp.API.Endpoints.ChangePasswordRequest
import com.example.datingapp.API.Endpoints.ChangePasswordResponse
import com.example.datingapp.API.Endpoints.LoginRequest
import com.example.datingapp.API.Endpoints.LoginResponse
import com.example.datingapp.API.Endpoints.RegisterRequest
import com.example.datingapp.API.Endpoints.RegisterResponse
import com.example.datingapp.API.Endpoints.UpdateUserRequest
import com.example.datingapp.API.Endpoints.UpdateUserResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("v1/imagestest/photo/{userGuid}")
    fun getUserPhoto(
        @Path("userGuid") userGuid: String
    ): Call<ResponseBody>


    // Função POST para dados genéricos (com cabeçalhos e corpo)
    @POST
    fun postData(@Url url: String, @HeaderMap headers: Map<String, String>, @Body body: String): Call<String>

    // Função PUT com cabeçalhos dinâmicos e corpo
    @PUT
    fun put(@Url url: String, @HeaderMap headers: Map<String, String>, @Body body: String): Call<String>
}

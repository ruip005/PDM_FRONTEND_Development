package com.example.datingapp.API

import com.example.datingapp.API.Endpoints.LoginRequest
import com.example.datingapp.API.Endpoints.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

interface ApiService {

    // Função GET com cabeçalhos dinâmicos
    @GET
    fun get(@Url url: String, @HeaderMap headers: Map<String, String>): Call<String>

    // Função POST para login (com corpo)
    @POST("v1/auth/login")  // Exemplo de especificação da URL, pode ser ajustado conforme necessário
    fun login(@Body body: LoginRequest): Call<LoginResponse>

    // Função POST para dados genéricos (com cabeçalhos e corpo)
    @POST
    fun postData(@Url url: String, @HeaderMap headers: Map<String, String>, @Body body: String): Call<String>

    // Função PUT com cabeçalhos dinâmicos e corpo
    @PUT
    fun put(@Url url: String, @HeaderMap headers: Map<String, String>, @Body body: String): Call<String>
}

package com.example.datingapp.API

import com.example.datingapp.API.Endpoints.Message
import com.example.datingapp.API.Endpoints.SwipeResponse
import com.example.datingapp.Classes.GenericMessage
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SwipeApiService {
    @GET("DESAFIO_API/rest/SWIPE/Messages")
    fun getMessages(): Call<SwipeResponse>

    @POST("DESAFIO_API/rest/SWIPE/Send")
    fun sendMessage(@Body message: GenericMessage): Call<GenericMessage>
}
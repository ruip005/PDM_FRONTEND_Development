package com.example.datingapp.API

import com.example.datingapp.Database.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SwipeApiService {
    @GET("DESAFIO_API/rest/SWIPE/Messages")
    fun getMessages(): Call<List<Message>>

    @POST("DESAFIO_API/rest/SWIPE/Send")
    fun sendMessage(@Body message: Message): Call<Message>
}
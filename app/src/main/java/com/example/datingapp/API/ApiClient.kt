package com.example.datingapp.API

import com.example.datingapp.API.Endpoints.LoginRequest
import com.example.datingapp.API.Endpoints.LoginResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ApiClient {

    private const val BASE_URL = "https://api.triumphmc.tech"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    fun get(url: String, headers: Map<String, String>, callback: (response: String?, error: String?) -> Unit) {
        val call = apiService.get(url, headers)
        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                callback(null, t.message)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Erro: ${response.message()}")
                }
            }
        })
    }

    fun post(url: String, headers: Map<String, String>, body: String, callback: (response: String?, error: String?) -> Unit) {
        val call = apiService.postData(url, headers, body)
        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                callback(null, t.message)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Erro: ${response.message()}")
                }
            }
        })
    }

    fun login(loginRequest: LoginRequest, callback: (response: LoginResponse?, error: String?) -> Unit) {
        val call = apiService.login(loginRequest)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(null, t.message)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Erro: ${response.message()}")
                }
            }
        })
    }

    fun put(url: String, headers: Map<String, String>, body: String, callback: (response: String?, error: String?) -> Unit) {
        val call = apiService.put(url, headers, body)
        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                callback(null, t.message)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Erro: ${response.message()}")
                }
            }
        })
    }
}

/* EXEMPLO DE USO
    // Exemplo de uso

val url = "https://api.triumphmc.tech/resource"
val headers = mapOf("Authorization" to "Bearer YOUR_TOKEN")

// Exemplo GET
ApiClient.get(url, headers) { response, error ->
    if (error != null) {
        println("Erro: $error")
    } else {
        println("Resposta: $response")
    }
}

// Exemplo POST
val requestBody = """{"key": "value"}"""
ApiClient.post(url, headers, requestBody) { response, error ->
    if (error != null) {
        println("Erro: $error")
    } else {
        println("Resposta: $response")
    }
}

// Exemplo PUT
val updateBody = """{"key": "newValue"}"""
ApiClient.put(url, headers, updateBody) { response, error ->
    if (error != null) {
        println("Erro: $error")
    } else {
        println("Resposta: $response")
    }
}

 */
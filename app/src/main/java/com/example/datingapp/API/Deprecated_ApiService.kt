package com.example.datingapp.API

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object Deprecated_ApiService { // Malta este arquivo serve para fazer requisições HTTP á API

    private val client = OkHttpClient()

    // Função GET
    fun get(
        url: String,
        headers: Map<String, String> = emptyMap(),
        callback: (response: String?, error: String?) -> Unit
    ) {
        val requestBuilder = Request.Builder().url(url)

        // Adiciona os headers personalizados
        headers.forEach { (key, value) -> requestBuilder.addHeader(key, value) }

        val request = requestBuilder.build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.body?.string(), null)
            }
        })
    }

    // Função POST
    fun post(
        url: String,
        headers: Map<String, String> = emptyMap(),
        body: String = "",
        callback: (response: String?, error: String?) -> Unit
    ) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = body.toRequestBody(mediaType)

        val requestBuilder = Request.Builder()
            .url(url)
            .post(requestBody)

        // Adiciona os headers personalizados
        headers.forEach { (key, value) -> requestBuilder.addHeader(key, value) }

        val request = requestBuilder.build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.body?.string(), null)
            }
        })
    }


    // Função PUT
    fun put(
        url: String,
        headers: Map<String, String> = emptyMap(),
        body: String = "",
        callback: (response: String?, error: String?) -> Unit
    ) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = body.toRequestBody(mediaType)

        val requestBuilder = Request.Builder()
            .url(url)
            .put(requestBody)

        // Adiciona os headers personalizados
        headers.forEach { (key, value) -> requestBuilder.addHeader(key, value) }

        val request = requestBuilder.build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.body?.string(), null)
            }
        })
    }
}

/* EXEMPLO DE USO
    val url = "https://api.example.com/resource"
    val headers = mapOf("Authorization" to "Bearer YOUR_TOKEN")

    // Exemplo GET
    ApiService.get(url, headers) { response, error ->
        if (error != null) {
            println("Erro: $error")
        } else {
            println("Resposta: $response")
        }
    }

    // Exemplo POST
    val requestBody = """{"key": "value"}"""
    ApiService.post(url, headers, requestBody) { response, error ->
        if (error != null) {
            println("Erro: $error")
        } else {
            println("Resposta: $response")
        }
    }

    // Exemplo PUT
    val updateBody = """{"key": "newValue"}"""
    ApiService.put(url, headers, updateBody) { response, error ->
        if (error != null) {
            println("Erro: $error")
        } else {
            println("Resposta: $response")
        }
    }
}
 */
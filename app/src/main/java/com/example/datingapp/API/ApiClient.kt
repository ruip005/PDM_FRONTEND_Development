package com.example.datingapp.API

import com.example.datingapp.API.Endpoints.LoginRequest
import com.example.datingapp.API.Endpoints.LoginResponse
import com.example.datingapp.API.Endpoints.RegisterRequest
import com.example.datingapp.API.Endpoints.RegisterResponse
import com.google.gson.Gson
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
        val call = apiService.login(loginRequest)  // Passa o objeto diretamente
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Se houver falha na comunicação, retorna o erro
                callback(null, "Erro de Comunicação: ${t.message}")
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Verifica se a resposta HTTP é bem-sucedida
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        if (loginResponse.success) {
                            // Se o login for bem-sucedido, devolve o token
                            callback(loginResponse, null)
                        } else {
                            // Se o sucesso for false, devolve a mensagem de erro
                            callback(null, loginResponse.message)
                        }
                    } else {
                        // Se o corpo da resposta for nulo, trata como erro genérico
                        callback(null, "Erro: Resposta inesperada.")
                    }
                } else {
                    // Caso a resposta HTTP não seja bem-sucedida (ex. 404, 500), trata o corpo da resposta
                    try {
                        // Tenta converter o corpo de erro para LoginResponse (mesmo quando a resposta não é 200)
                        val errorResponse = Gson().fromJson(response.errorBody()?.string(), LoginResponse::class.java)
                        // Retorna a mensagem de erro contida no campo "message"
                        callback(null, errorResponse.message)
                    } catch (e: Exception) {
                        // Se a conversão falhar, retorna uma mensagem genérica
                        callback(null, "Erro desconhecido")
                    }
                }
            }
        })
    }

    fun register(registerRequest: RegisterRequest, callback: (response: RegisterResponse?, error: String?) -> Unit) {
        val call = apiService.register(registerRequest)  // Passa o objeto diretamente
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                // Se houver falha na comunicação, retorna o erro
                callback(null, "Erro de Comunicação: ${t.message}")
            }

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                // Verifica se a resposta HTTP é bem-sucedida
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        if (registerResponse.success) {
                            // Se o login for bem-sucedido, devolve o token
                            callback(registerResponse, null)
                        } else {
                            // Se o sucesso for false, devolve a mensagem de erro
                            callback(null, registerResponse.message)
                        }
                    } else {
                        // Se o corpo da resposta for nulo, trata como erro genérico
                        callback(null, "Erro: Resposta inesperada.")
                    }
                } else {
                    // Caso a resposta HTTP não seja bem-sucedida (ex. 404, 500), trata o corpo da resposta
                    try {
                        // Tenta converter o corpo de erro para RegisterResponse (mesmo quando a resposta não é 200)
                        val errorResponse = Gson().fromJson(response.errorBody()?.string(), RegisterResponse::class.java)
                        // Retorna a mensagem de erro contida no campo "message"
                        callback(null, errorResponse.message)
                    } catch (e: Exception) {
                        // Se a conversão falhar, retorna uma mensagem genérica
                        callback(null, "Erro desconhecido")
                    }
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
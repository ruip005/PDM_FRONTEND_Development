package com.example.datingapp.API

import android.content.Context
import com.example.datingapp.API.Endpoints.ChangePasswordRequest
import com.example.datingapp.API.Endpoints.ChangePasswordResponse
import com.example.datingapp.API.Endpoints.GenericResponse
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
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import com.example.datingapp.API.Endpoints.Message
import com.example.datingapp.API.Endpoints.SwipeResponse

object ApiClient {

    private const val BASE_URL = "https://api.triumphmc.tech"
    private const val SWIPE_BASE_URL = "https://personal-b99makny.outsystemscloud.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val swipeRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SWIPE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)
    private val swipeApiService: SwipeApiService = swipeRetrofit.create(SwipeApiService::class.java)

    // Métodos para a nova API
    fun getMessages(callback: (messages: List<Message>?, error: String?) -> Unit) {
        val call = swipeApiService.getMessages()
        call.enqueue(object : Callback<SwipeResponse> {
            override fun onFailure(call: Call<SwipeResponse>, t: Throwable) {
                callback(null, "Erro ao buscar mensagens: ${t.message}")
            }

            override fun onResponse(call: Call<SwipeResponse>, response: Response<SwipeResponse>) {
                if (response.isSuccessful) {
                    val messages = response.body()?.MessagesList
                    callback(messages, null)
                } else {
                    callback(null, "Erro ao buscar mensagens: ${response.message()}")
                }
            }
        })
    }

    fun sendMessage(message: Message, callback: (response: Message?, error: String?) -> Unit) {
        val call = swipeApiService.sendMessage(message)
        call.enqueue(object : Callback<Message> {
            override fun onFailure(call: Call<Message>, t: Throwable) {
                callback(null, "Erro ao enviar mensagem: ${t.message}")
            }

            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                println(response)
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Erro ao enviar mensagem: ${response.message()}")
                }
            }
        })
    }


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

    //
    fun getUser(
        context: Context,
        userId: String,
        request: GetProfileRequest,
        callback: (response: GetProfileResponse?, error: String?) -> Unit
    ) {
        // Obtém o token JWT armazenado
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("SESSION_TOKEN", null)

        if (sessionToken.isNullOrEmpty()) {
            // Retorna um erro se o token não estiver disponível
            callback(null, "Token JWT não encontrado.")
            return
        }

        // Chama o endpoint
        val call = apiService.getUser(userId, request)
        call.enqueue(object : Callback<GetProfileResponse> {
            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                // Trata falha na comunicação
                callback(null, "Erro de Comunicação: ${t.message}")
            }

            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                // Verifica se a resposta HTTP é bem-sucedida
                if (response.isSuccessful) {
                    val getUserResponse = response.body()
                    if (getUserResponse != null) {
                        if (getUserResponse.success) {
                            // Se for sucesso, retorna o objeto de resposta
                            callback(getUserResponse, null)
                        } else {
                            // Se `success` for false, retorna a mensagem de erro
                            callback(null, getUserResponse.message)
                        }
                    } else {
                        // Corpo de resposta nulo
                        callback(null, "Erro: Resposta inesperada.")
                    }
                } else {
                    // Trata erros HTTP
                    try {
                        val errorResponse = Gson().fromJson(
                            response.errorBody()?.string(),
                            GetProfileResponse::class.java
                        )
                        callback(null, errorResponse.message)
                    } catch (e: Exception) {
                        callback(null, "Erro desconhecido.")
                    }
                }
            }
        })
    }

    //

    fun changePassword(
        context: Context,
        request: ChangePasswordRequest,
        callback: (response: ChangePasswordResponse?, error: String?) -> Unit
    ) {
        // Obtém o token JWT armazenado
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("SESSION_TOKEN", null)

        if (sessionToken.isNullOrEmpty()) {
            // Retorna um erro se o token não estiver disponível
            callback(null, "Token JWT não encontrado.")
            return
        }

        // Chama o endpoint
        val call = apiService.changePassword(request, "Bearer $sessionToken")
        call.enqueue(object : Callback<ChangePasswordResponse> {
            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                // Trata falha na comunicação
                callback(null, "Erro de Comunicação: ${t.message}")
            }

            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>
            ) {
                // Verifica se a resposta HTTP é bem-sucedida
                if (response.isSuccessful) {
                    val changePasswordResponse = response.body()
                    if (changePasswordResponse != null) {
                        if (changePasswordResponse.success) {
                            // Se for sucesso, retorna o objeto de resposta
                            callback(changePasswordResponse, null)
                        } else {
                            // Se `success` for false, retorna a mensagem de erro
                            callback(null, changePasswordResponse.message)
                        }
                    } else {
                        // Corpo de resposta nulo
                        callback(null, "Erro: Resposta inesperada.")
                    }
                } else {
                    // Trata erros HTTP
                    try {
                        val errorResponse = Gson().fromJson(
                            response.errorBody()?.string(),
                            ChangePasswordResponse::class.java
                        )
                        callback(null, errorResponse.message)
                    } catch (e: Exception) {
                        callback(null, "Erro desconhecido.")
                    }
                }
            }
        })
    }

    fun updateUser(
        context: Context,
        request: UpdateUserRequest,
        callback: (response: UpdateUserResponse?, error: String?) -> Unit
    ) {
        // Obtém o token JWT armazenado
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("SESSION_TOKEN", null)

        if (sessionToken.isNullOrEmpty()) {
            // Retorna um erro se o token não estiver disponível
            callback(null, "Token JWT não encontrado.")
            return
        }

        // Chama o endpoint
        val call = apiService.updateUser(request, "Bearer $sessionToken")
        call.enqueue(object : Callback<UpdateUserResponse> {
            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                // Trata falha na comunicação
                callback(null, "Erro de Comunicação: ${t.message}")
            }

            override fun onResponse(
                call: Call<UpdateUserResponse>,
                response: Response<UpdateUserResponse>
            ) {
                // Verifica se a resposta HTTP é bem-sucedida
                if (response.isSuccessful) {
                    val updateUserResponse = response.body()
                    if (updateUserResponse != null) {
                        if (updateUserResponse.success) {
                            // Se for sucesso, retorna o objeto de resposta
                            callback(updateUserResponse, null)
                        } else {
                            // Se `success` for false, retorna a mensagem de erro
                            callback(null, updateUserResponse.message)
                        }
                    } else {
                        // Corpo de resposta nulo
                        callback(null, "Erro: Resposta inesperada.")
                    }
                } else {
                    // Trata erros HTTP
                    try {
                        val errorResponse = Gson().fromJson(
                            response.errorBody()?.string(),
                            UpdateUserResponse::class.java
                        )
                        callback(null, errorResponse.message)
                    } catch (e: Exception) {
                        callback(null, "Erro desconhecido.")
                    }
                }
            }
        })
    }

    fun getProfile(
        userId: String,
        request: GetProfileRequest,
        callback: (response: GetProfileResponse?, error: String?) -> Unit
    ) {
        val call = apiService.getProfile(userId, request)
        call.enqueue(object : Callback<GetProfileResponse> {
            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                callback(null, "Erro de Comunicação: ${t.message}")
            }

            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) {
                            callback(it, null)
                        } else {
                            callback(null, it.message)
                        }
                    } ?: callback(null, "Erro: Resposta inesperada.")
                } else {
                    callback(null, "Erro HTTP: Código ${response.code()}")
                }
            }
        })
    }

    fun uploadProfilePicture(
        context: Context,
        imageFile: File?,
        callback: (response: UploadProfileResponse?, error: String?) -> Unit
    ) {
        // Obtém o token JWT armazenado
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("SESSION_TOKEN", null)

        if (sessionToken.isNullOrEmpty()) {
            // Retorna um erro se o token não estiver disponível
            callback(null, "Token JWT não encontrado.")
            return
        }

        // Prepara a imagem como MultipartBody.Part, caso exista
        val imagePart = imageFile?.let {
            val requestBody = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, requestBody)
        }

        // Chama o endpoint
        val call = apiService.uploadProfilePicture("Bearer $sessionToken", imagePart)
        call.enqueue(object : Callback<UploadProfileResponse> {
            override fun onFailure(call: Call<UploadProfileResponse>, t: Throwable) {
                callback(null, "Erro de Comunicação: ${t.message}")
            }

            override fun onResponse(
                call: Call<UploadProfileResponse>,
                response: Response<UploadProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    if (uploadResponse != null && uploadResponse.success) {
                        // Retorna a resposta de sucesso
                        callback(uploadResponse, null)
                    } else {
                        // Trata a resposta com sucesso = false
                        val message = uploadResponse?.message ?: "Erro: Resposta inesperada."
                        callback(null, message)
                    }
                } else {
                    // Trata erro HTTP
                    callback(null, "Erro HTTP: Código ${response.code()}")
                }
            }
        })
    }

    fun getUserPhoto(
        userGuid: String,
        callback: (imageData: ByteArray?, error: String?) -> Unit
    ) {
        val call = apiService.getUserPhoto(userGuid)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Trata falha na comunicação
                callback(null, "Erro de Comunicação: ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val contentType = response.headers()["Content-Type"]
                    if (contentType == "image/jpeg") {
                        // Caso seja uma imagem, retorna os dados em byte array
                        val imageBytes = response.body()?.bytes()
                        if (imageBytes != null) {
                            callback(imageBytes, null)
                        } else {
                            callback(null, "Erro: Resposta de imagem está vazia.")
                        }
                    } else {
                        // Caso seja um JSON, processa a mensagem de erro
                        try {
                            val errorResponse = Gson().fromJson(
                                response.body()?.string(),
                                GenericResponse::class.java
                            )
                            callback(null, errorResponse.message)
                        } catch (e: Exception) {
                            callback(null, "Erro desconhecido.")
                        }
                    }
                } else {
                    callback(null, "Erro HTTP: Código ${response.code()}")
                }
            }
        })
    }

    fun getNewUserGuid(
        context: Context,
        callback: (newGuid: String?, error: String?) -> Unit
    ) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("SESSION_TOKEN", null)

        if (sessionToken.isNullOrEmpty()) {
            callback(null, "Token JWT não encontrado.")
            return
        }

        val call = apiService.getNewUserGuid("Bearer $sessionToken")
        call.enqueue(object : Callback<GetNewUserGuidResponse> {
            override fun onFailure(call: Call<GetNewUserGuidResponse>, t: Throwable) {
                callback(null, "Erro ao obter novo GUID: ${t.message}")
            }

            override fun onResponse(
                call: Call<GetNewUserGuidResponse>,
                response: Response<GetNewUserGuidResponse>
            ) {
                if (response.isSuccessful) {
                    val newGuidResponse = response.body()
                    if (newGuidResponse?.success == true) {
                        callback(newGuidResponse.newGuid, null)
                    } else {
                        callback(null, "Erro ao obter GUID: ${newGuidResponse?.message}")
                    }
                } else {
                    callback(null, "Erro desconhecido ao obter GUID.")
                }
            }
        })
    }

    // Dislike or Like user
    fun decisionUser(
        context: Context,
        userGuid: String,
        decision: String,
        callback: (error: String?) -> Unit
    ) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("SESSION_TOKEN", null)

        if (sessionToken.isNullOrEmpty()) {
            callback("Token JWT não encontrado.")
            return
        }
        // userIdentificator, decision
        val body = JSONObject()
        body.put("userIdentificator", userGuid)
        body.put("decision", decision)
        //DialogUtils.showErrorPopup(ProfileActivity::class::java, "JSON", body.toString())
        val call = apiService.decisionUser("Bearer $sessionToken", body.toString())
        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                callback("Erro ao enviar decisão: ${t.message}")
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    callback(null)
                } else {
                    callback("Erro desconhecido ao enviar decisão.")
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
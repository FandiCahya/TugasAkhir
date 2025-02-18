package com.example.applicationsop.Api

// File: ApiClient.kt

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.example.applicationsop.models.LoginRequest
import com.example.applicationsop.models.LoginResponse
import io.ktor.client.call.body
import com.example.applicationsop.core.ApiConfig

// Inisialisasi HttpClient dengan plugin ContentNegotiation untuk JSON
val client = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })  // Menangani JSON dengan Kotlinx Serialization
    }
}

suspend fun loginUser(email: String, password: String): LoginResponse? {
    return try {
        // Mengirimkan request POST untuk login dan mengembalikan LoginResponse
        val response: HttpResponse = client.post("${ApiConfig.BASE_URL}login") {
            contentType(ContentType.Application.Json)  // Menentukan jenis konten yang dikirim
            setBody(LoginRequest(email, password))  // Mengirimkan data login dalam body request
        }

        // Mengambil body dari response dan mengonversinya menjadi LoginResponse
        response.body<LoginResponse>()
    } catch (e: Exception) {
        // Tangani error jika ada masalah dengan koneksi atau API
        e.printStackTrace()
        null
    }
}

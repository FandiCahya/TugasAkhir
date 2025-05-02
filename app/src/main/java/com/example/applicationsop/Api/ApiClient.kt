package com.example.applicationsop.Api

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
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.HttpTimeout

// Inisialisasi HttpClient dengan plugin ContentNegotiation untuk JSON
val client = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })  // Menangani JSON dengan Kotlinx Serialization
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }

}

// Function to login
suspend fun loginUser(email: String, password: String): LoginResponse? {
    return try {
        val response: HttpResponse = client.post("${ApiConfig.BASE_URL}login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }

        // Log the response body for debugging
        val responseBody = response.bodyAsText()
        println("Data Response Body: $responseBody")  // This will show the full response in logcat

        if (response.status == HttpStatusCode.OK) {
            response.body<LoginResponse>()
        } else {
            println("Login failed with status: ${response.status}")
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

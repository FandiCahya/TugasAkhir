package com.example.applicationsop.Api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.example.applicationsop.core.ApiConfig
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.*

val logout = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })  // Menangani JSON dengan Kotlinx Serialization
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

// Function to logout
suspend fun logoutUser(token: String): Boolean {
    return try {
        val response: HttpResponse = logout.post("${ApiConfig.BASE_URL}logout") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $token")  // Menambahkan Bearer Token pada header
        }

        // Log the response body for debugging
        val responseBody = response.bodyAsText()
        println("Data Response Body: $responseBody")  // This will show the full response in logcat

        if (response.status == HttpStatusCode.OK) {
            println("Logout successful")
            true
        } else {
            println("Logout failed with status: ${response.status}")
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

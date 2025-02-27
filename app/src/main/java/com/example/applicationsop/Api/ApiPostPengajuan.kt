package com.example.applicationsop.Api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.core.ApiConfig
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import com.example.applicationsop.models.PengajuanRequest
import io.ktor.client.statement.bodyAsText

// Initialize HttpClient with JSON support
val PostPengajuan = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

// Function to submit Pengajuan via POST request
suspend fun postPengajuan(pengajuanRequest: PengajuanRequest): HttpResponse {
    return try {
        // Make the POST request to the API
        val response: HttpResponse = PostPengajuan.post("${ApiConfig.BASE_URL}pengajuan") {
            contentType(ContentType.Application.Json)
            setBody(pengajuanRequest)  // Send the PengajuanRequest as body
        }

        // Handle successful response
        if (response.status.value in 200..299) {
            println("Successfully submitted Pengajuan!")
        } else {
            val responseBody = response.bodyAsText()  // Mengambil body response untuk debug
            println("Failed to submit Pengajuan: $responseBody")
        }

        response  // Return the response object to check the result

    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        throw Exception("Failed to submit Pengajuan")
    }
}

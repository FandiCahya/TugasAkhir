package com.example.applicationsop.Api

import android.content.Context
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.core.ApiConfig
import com.example.applicationsop.core.UserUtils
import kotlinx.serialization.json.Json
import io.ktor.client.statement.HttpResponse
import com.example.applicationsop.models.PengembanganRequest
import io.ktor.client.statement.bodyAsText

// Initialize HttpClient with JSON support
val PostPengembangan = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

// Function to submit Pengembangan via POST request
suspend fun postPengembangan(context: Context, pengembanganRequest: PengembanganRequest): HttpResponse {
    return try {
        val userData = UserUtils.getUserData(context)
        val token = userData["token"]

        val response: HttpResponse = PostPengembangan.post("${ApiConfig.BASE_URL}pengembangan") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(pengembanganRequest)
        }

        val responseBody = response.bodyAsText()
        if (response.status.value in 200..299) {
            println("Successfully submitted Pengembangan!")
        } else {
            println("Failed to submit Pengembangan: $responseBody")
        }

        response

    } catch (e: Exception) {
        e.printStackTrace()
        throw Exception("Failed to submit Pengembangan")
    }
}


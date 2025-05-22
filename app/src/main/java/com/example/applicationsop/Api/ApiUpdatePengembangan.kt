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
import com.example.applicationsop.models.UpdatePengembangan
import io.ktor.client.statement.bodyAsText

// Initialize HttpClient with JSON support
val PutPengembangan = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

// Function to update Pengembangan via PUT request
suspend fun updatePengembangan(context: Context, id: String, updatePengembangan: UpdatePengembangan): HttpResponse {
    return try {
        val userData = UserUtils.getUserData(context)
        val token = userData["token"]

        val response: HttpResponse = PutPengembangan.put("${ApiConfig.BASE_URL}pengembangan/$id") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(updatePengembangan)
        }

        if (response.status.value in 200..299) {
            println("Successfully updated Pengembangan!")
        } else {
            val responseBody = response.bodyAsText()
            println("Failed to update Pengembangan: $responseBody")
        }

        response

    } catch (e: Exception) {
        e.printStackTrace()
        throw Exception("Failed to update Pengembangan")
    }
}


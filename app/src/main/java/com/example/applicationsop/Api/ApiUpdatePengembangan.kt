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
suspend fun updatePengembangan(id: String, updatePengembangan: UpdatePengembangan): HttpResponse {
    return try {
        // Make the PUT request to the API
        val response: HttpResponse = PutPengembangan.put("${ApiConfig.BASE_URL}pengembangan/$id") {
            contentType(ContentType.Application.Json)
            setBody(updatePengembangan)  // Send the UpdatePengembangan as body
        }

        // Handle successful response
        if (response.status.value in 200..299) {
            println("Successfully updated Pengembangan!")
        } else {
            val responseBody = response.bodyAsText()  // Get response body for debugging
            println("Failed to update Pengembangan: $responseBody")
        }

        response  // Return the response object to check the result

    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        throw Exception("Failed to update Pengembangan")
    }
}

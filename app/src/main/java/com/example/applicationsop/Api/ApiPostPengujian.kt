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
import com.example.applicationsop.models.PengujianRequest
import io.ktor.client.statement.bodyAsText

// Initialize HttpClient with JSON support
val PostPengujian = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 20000
    }
}

// Function to submit Pengujian via POST request
suspend fun postPengujian(pengujianRequest: PengujianRequest): HttpResponse {
    return try {
        // Make the POST request to the API
        val response: HttpResponse = PostPengujian.post("${ApiConfig.BASE_URL}pengujian") {
            contentType(ContentType.Application.Json)
            setBody(pengujianRequest)  // Send the PengujianRequest as body
        }
        val responseBody = response.bodyAsText()
        val responseStatus = response.status
        val responseHeaders = response.headers

        println("Response Status: $responseStatus")
        println("Response Headers: $responseHeaders")
        println("Response Body: $responseBody")

        // Handle successful response
        if (response.status.value in 200..299) {
            val responseBody = response.bodyAsText()
            println("Successfully submitted Pengujian!")
            println("Response: $responseBody")
        } else {
            val responseBody = response.bodyAsText()  // Get the response body for debugging
            println("Failed to submit Pengujian: $responseBody")
        }

        response  // Return the response object to check the result

    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        throw Exception("Failed to submit Pengujian")
    }
}

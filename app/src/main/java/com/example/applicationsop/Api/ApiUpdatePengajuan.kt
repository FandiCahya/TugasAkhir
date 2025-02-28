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

val PutPengajuan = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

// Function to update Pengajuan via PUT request
suspend fun updatePengajuan(id: String, pengajuanRequest: PengajuanRequest): HttpResponse {
    return try {
        // Make the PUT request to the API
        val response: HttpResponse = PutPengajuan.put("${ApiConfig.BASE_URL}pengajuan/$id") {
            contentType(ContentType.Application.Json)
            setBody(pengajuanRequest)  // Send the PengajuanRequest as body
        }

        // Handle successful response
        if (response.status.value in 200..299) {
            println("Successfully updated Pengajuan!")
        } else {
            val responseBody = response.bodyAsText()  // Get response body for debugging
            println("Failed to update Pengajuan: $responseBody")
        }

        response  // Return the response object to check the result

    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        throw Exception("Failed to update Pengajuan")
    }
}

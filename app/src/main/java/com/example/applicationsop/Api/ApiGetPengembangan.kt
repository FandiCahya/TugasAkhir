package com.example.applicationsop.Api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.models.Pengembangan
import com.example.applicationsop.core.ApiConfig
import com.example.applicationsop.models.ResponsePengembangan
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

// Initialize HttpClient with JSON support
val GetPengembangan = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })  // Handle JSON with Kotlinx serialization
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

// Function to fetch pengembangan list
suspend fun fetchPengembanganList(): List<Pengembangan> {
    return try {
        val response: HttpResponse = GetPengembangan.get("${ApiConfig.BASE_URL}pengembangan") {
            contentType(ContentType.Application.Json)
        }

        if (response.status.value in 200..299) {
            println("Successful response Pengembangan!")
        }

        // Deserialize the response body into ResponsePengajuan
        val responsePengembangan: ResponsePengembangan = response.body()
        println("Pengembangan List: ${responsePengembangan.payload}")

        // Filter the payload to exclude items with the status 'finished'
        val filteredPengembanganList = responsePengembangan.payload.filter { pengembangan ->
            pengembangan.status != "finished"  // Filter out 'finished' status
        }


// Return the list of Pengembangan
        filteredPengembanganList
    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        emptyList()  // Return an empty list on error
    }
}

suspend fun fetchPengembanganSortList(role: String? = null, devisi: String? = null,userId: String? = null): List<Pengembangan> {
    return try {
        val url = buildString {
            append("${ApiConfig.BASE_URL}pengembangan?")
            if (role != null) append("role=$role&")
            if (devisi != null) append("devisi=$devisi&")
            if (userId != null) append("userId=$userId&")

            // Remove the trailing '&' if any query parameters were added
            if (endsWith("&")) deleteCharAt(length - 1)
        }

        // Make the GET request with the built URL
        val response: HttpResponse = GetPengembangan.get(url) {
            contentType(ContentType.Application.Json)
        }

        if (response.status.value in 200..299) {
            println("Successful response Pengembangan!")
        }

        // Deserialize the response body into ResponsePengajuan
        val responsePengembangan: ResponsePengembangan = response.body()
        println("Pengembangan List: ${responsePengembangan.payload}")

        responsePengembangan.payload // Return the list of Pengembangan
    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        emptyList()  // Return an empty list on error
    }
}

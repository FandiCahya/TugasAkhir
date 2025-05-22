package com.example.applicationsop.Api

import android.content.Context
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.models.Pengembangan
import com.example.applicationsop.core.ApiConfig
import com.example.applicationsop.core.UserUtils
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
suspend fun fetchPengembanganList(context: Context): List<Pengembangan> {
    return try {
        val userData = UserUtils.getUserData(context)
        val token = userData["token"]
        println(token)

        val response: HttpResponse = GetPengembangan.get("${ApiConfig.BASE_URL}pengembangan") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        if (response.status.value in 200..299) {
            println("Successful response Pengembangan!")
        }

        val rawResponse = response.body<String>()
        println("Raw Response: $rawResponse")

        val responsePengembangan: ResponsePengembangan = response.body()
        println("Pengembangan List: ${responsePengembangan.payload}")

        responsePengembangan.payload.filter { it.status != "finished" }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

suspend fun fetchPengembanganSortList(
    context: Context,
    role: String? = null,
    devisi: String? = null,
    userId: String? = null
): List<Pengembangan> {
    return try {
        val userData = UserUtils.getUserData(context)
        val token = userData["token"]

        val url = buildString {
            append("${ApiConfig.BASE_URL}pengembangan?")
            if (role != null) append("role=$role&")
            if (devisi != null) append("devisi=$devisi&")
            if (userId != null) append("userId=$userId&")
            if (endsWith("&")) deleteCharAt(length - 1)
        }

        val response: HttpResponse = GetPengembangan.get(url) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        if (response.status.value in 200..299) {
            println("Successful response Pengembangan!")
        }

        val responsePengembangan: ResponsePengembangan = response.body()
        println("Pengembangan List: ${responsePengembangan.payload}")

        responsePengembangan.payload.filter { it.status != "finished" }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}


package com.example.applicationsop.Api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.models.Pengajuan
import com.example.applicationsop.core.ApiConfig
import com.example.applicationsop.models.ResponsePengajuan
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

// Initialize HttpClient with JSON support
val GetPengajuan = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })  // Handle JSON with Kotlinx serialization
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

// Function to fetch Pengajuan list
suspend fun fetchPengajuanList(): List<Pengajuan> {
    return try {
        val response: HttpResponse = GetPengajuan.get("${ApiConfig.BASE_URL}pengajuan") {
            contentType(ContentType.Application.Json)
        }

        if (response.status.value in 200..299) {
            println("Successful response!")
        }

        // Deserialize the response body into ResponsePengajuan
        val responsePengajuan: ResponsePengajuan = response.body()
        println("Pengajuan List: ${responsePengajuan.payload}")

        responsePengajuan.payload // Return the list of pengajuan
    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        emptyList()  // Return an empty list on error
    }
}


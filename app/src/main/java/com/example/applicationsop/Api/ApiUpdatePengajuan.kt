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
suspend fun updatePengajuan(context: Context, id: String, pengajuanRequest: PengajuanRequest): HttpResponse {
    return try {
        val userData = UserUtils.getUserData(context)
        val token = userData["token"]

        val response: HttpResponse = PutPengajuan.put("${ApiConfig.BASE_URL}pengajuan/$id") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(pengajuanRequest)
        }

        if (response.status.value in 200..299) {
            println("Successfully updated Pengajuan!")
        } else {
            val responseBody = response.bodyAsText()
            println("Failed to update Pengajuan: $responseBody")
        }

        response

    } catch (e: Exception) {
        e.printStackTrace()
        throw Exception("Failed to update Pengajuan")
    }
}


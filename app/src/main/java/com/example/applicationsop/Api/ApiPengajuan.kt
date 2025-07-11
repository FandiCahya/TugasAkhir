package com.example.applicationsop.Api

import android.content.Context
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.models.Pengajuan
import com.example.applicationsop.core.ApiConfig
import com.example.applicationsop.core.UserUtils
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

suspend fun fetchPengajuanList(
    context: Context,
    status: String? = null,
    role: String? = null,
    devisi: String? = null
): List<Pengajuan> {
    return try {
        val userData = UserUtils.getUserData(context)
        val token = userData["token"]as? String

        val url = buildString {
            append("${ApiConfig.BASE_URL}pengajuan?")
            if (status != null) append("status=$status&")
            if (role != null) append("role=$role&")
            if (devisi != null) append("devisi=$devisi&")
            if (endsWith("&")) deleteCharAt(length - 1)
        }

        val response: HttpResponse = GetPengajuan.get(url) {
            contentType(ContentType.Application.Json)
            if (!token.isNullOrEmpty()) {
                headers {
                    bearerAuth(token)
                }
            }
        }

        if (response.status.value in 200..299) {
            println("Successful response Get Pengajuan!")
        }

        val responsePengajuan: ResponsePengajuan = response.body()
        println("Pengajuan List: ${responsePengajuan.payload}")

        responsePengajuan.payload
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}





package com.example.applicationsop.Api

import android.content.Context
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.models.ResponseLaporan
import com.example.applicationsop.models.Laporan
import com.example.applicationsop.core.ApiConfig
import com.example.applicationsop.core.UserUtils
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

val GetLaporan = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

suspend fun fetchLaporanList(
    context: Context,
    status_pengajuan: String? = null,
    role: String? = null,
    devisi: String? = null,
    userId: String? = null
): List<Laporan> {
    return try {
        val userData = UserUtils.getUserData(context)
        val token = userData["token"]

        val url = buildString {
            append("${ApiConfig.BASE_URL}showall?")
            if (status_pengajuan != null) append("status_pengajuan=$status_pengajuan&")
            if (role != null) append("role=$role&")
            if (devisi != null) append("devisi=$devisi&")
            if (userId != null) append("userId=$userId&")
            if (endsWith("&")) deleteCharAt(length - 1)
        }

        val response: HttpResponse = GetLaporan.get(url) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        if (response.status.value in 200..299) {
            println("Successful response Get Laporan!")
        }

        val responseLaporan: ResponseLaporan = response.body()
        println("Laporan List: ${responseLaporan.payload}")

        responseLaporan.payload
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

package com.example.applicationsop.Api

import android.content.Context
import android.util.Log
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.core.ApiConfig
import com.example.applicationsop.core.UserUtils
import com.example.applicationsop.models.PengajuanRequest
import com.example.applicationsop.models.PersetujuanDetail
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import java.io.File

val PostPersetujuan = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

// Function to update Persetujuan via PUT request
suspend fun updatePersetujuanPengujian(
    context: Context,
    id: String,
    persetujuanRequest: PersetujuanDetail
): HttpResponse {
    return try {
        val userData = UserUtils.getUserData(context)
        val token = userData["token"]

        val response: HttpResponse = PostPersetujuan.post("${ApiConfig.BASE_URL}pengujian-detail/$id") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(persetujuanRequest)
        }

        if (response.status.value in 200..299) {
            println("Successfully updated Persetujuan Pengujian!")
        } else {
            val responseBody = response.bodyAsText()
            println("Failed to update Persetujuan Pengujian: $responseBody")
        }

        response

    } catch (e: Exception) {
        e.printStackTrace()
        throw Exception("Failed to update Persetujuan Pengujian")
    }
}

suspend fun UpdatePersetujuanDiterima(
    context: Context,
    id: String,
    persetujuanRequest: PersetujuanDetail,
    signatureFile: File
): HttpResponse {
    return try {
        val token = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
            .getString("token", null)

        val response: HttpResponse = PostPersetujuan.post("${ApiConfig.BASE_URL}pengujian-detail/$id") {
            contentType(ContentType.MultiPart.FormData)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("status", persetujuanRequest.status ?: "")
                        append("catatan", persetujuanRequest.catatan ?: "")
                        append("signature", signatureFile.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"${signatureFile.name}\"")
                        })
                    }
                )
            )
        }

        if (response.status.value in 200..299) {
            println("Successfully submitted Persetujuan!")
            Log.d("API Response", "Status: ${response.status}, Body: ${response.bodyAsText()}")
        } else {
            val responseBody = response.bodyAsText()
            println("Failed to submit Persetujuan: $responseBody")
            Log.e("API Error", "Status: ${response.status}, Body: $responseBody")
            throw Exception("Failed to submit Persetujuan")
        }

        response

    } catch (e: Exception) {
        e.printStackTrace()
        throw Exception("Failed to submit Persetujuan")
    }
}


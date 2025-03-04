package com.example.applicationsop.Api

import android.util.Log
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
import com.example.applicationsop.models.PengajuanRequest
import io.ktor.client.statement.bodyAsText
import io.ktor.client.request.forms.*
import java.io.File

// Initialize HttpClient with JSON support
val PostPengajuan = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 30000
    }
}

// Function to submit Pengajuan via POST request
suspend fun postPengajuan(pengajuanRequest: PengajuanRequest, signatureFile: File): HttpResponse {
    return try {
        // Make the POST request to the API
        val response: HttpResponse = PostPengajuan.post("${ApiConfig.BASE_URL}pengajuan") {
            contentType(ContentType.MultiPart.FormData)
            // Send multipart form-data
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("tgl", pengajuanRequest.tgl ?: "")
                        append("nama_sistem", pengajuanRequest.nama_sistem ?: "")
                        append("jenis", pengajuanRequest.jenis ?: "")
                        append("rencana_anggaran", pengajuanRequest.rencana_anggaran ?: "")
                        append("masalah", pengajuanRequest.masalah ?: "")
                        append("output", pengajuanRequest.output ?: "")
                        append("status", pengajuanRequest.status ?: "")
                        append("user_id", pengajuanRequest.user_id ?: "")

                        // Adding the signature image file as part of the form
                        append("signature", signatureFile.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "image/png")  // Modify the content type based on the image format
                            append(HttpHeaders.ContentDisposition, "filename=\"${signatureFile.name}\"")
                        })
                    }
                )
            )

        }

        // Handle successful response
        if (response.status.value in 200..299) {
            println("Successfully submitted Pengajuan!")
            Log.d("API Response", "Status: ${response.status}, Body: ${response.bodyAsText()}")
        } else {
            val responseBody = response.bodyAsText()  // Mengambil body response untuk debug
            println("Failed to submit Pengajuan: $responseBody")
            Log.e("API Error", "Status: ${response.status}, Body: $responseBody")
            throw Exception("Failed to submit Pengajuan")
        }

        response  // Return the response object to check the result

    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        throw Exception("Failed to submit Pengajuan")
    }
}

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
suspend fun updatePersetujuanPengujian(id: String, persetujuanRequest: PersetujuanDetail): HttpResponse {
    return try {
        // Make the PUT request to the API
        val response: HttpResponse = PostPersetujuan.post("${ApiConfig.BASE_URL}pengujian-detail/$id") {
            contentType(ContentType.Application.Json)
            setBody(persetujuanRequest)  // Send the PersetujuanRequest as body
        }

        // Handle successful response
        if (response.status.value in 200..299) {
            println("Successfully updated Persetujuan Pengujian!")
        } else {
            val responseBody = response.bodyAsText()  // Get response body for debugging
            println("Failed to update Persetujuan Pengujian: $responseBody")
        }

        response  // Return the response object to check the result

    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        throw Exception("Failed to update Persetujuan Pengujian")
    }
}

suspend fun UpdatePersetujuanDiterima(id: String, persetujuanRequest: PersetujuanDetail, signatureFile: File): HttpResponse {
    return try {
        // Make the POST request to the API
        val response: HttpResponse = PostPersetujuan.post("${ApiConfig.BASE_URL}pengujian-detail/$id") {
            contentType(ContentType.MultiPart.FormData)
            // Send multipart form-data
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("status", persetujuanRequest.status ?: "")

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
            println("Successfully submitted Persetujuan!")
            Log.d("API Response", "Status: ${response.status}, Body: ${response.bodyAsText()}")
        } else {
            val responseBody = response.bodyAsText()  // Mengambil body response untuk debug
            println("Failed to submit Persetujuan: $responseBody")
            Log.e("API Error", "Status: ${response.status}, Body: $responseBody")
            throw Exception("Failed to submit Persetujuan")
        }

        response  // Return the response object to check the result

    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        throw Exception("Failed to submit Persetujuan")
    }
}

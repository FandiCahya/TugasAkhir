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
import io.ktor.client.statement.HttpResponse
import com.example.applicationsop.models.PengujianRequest
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonElement

// Initialize HttpClient with JSON support
val PostPengujian = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 20000
    }
}

// Function to submit Pengujian via POST request
suspend fun postPengujian(context: Context, pengujianRequest: PengujianRequest): HttpResponse {
    return try {
        val userData = UserUtils.getUserData(context)
        val token = userData["token"]

        val response: HttpResponse = PostPengujian.post("${ApiConfig.BASE_URL}pengujian") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(pengujianRequest)
        }

        val responseBody = response.bodyAsText()
        val responseStatus = response.status
        val responseHeaders = response.headers

        println("Response Status: $responseStatus")
        println("Response Headers: $responseHeaders")
        println("Response Body: $responseBody")

        if (response.status.isSuccess()) {
            println("Successfully submitted Pengujian!")
        } else {
            println("Failed to submit Pengujian: $responseBody")
        }

        response

    } catch (e: Exception) {
        e.printStackTrace()
        throw Exception("Failed to submit Pengujian: ${e.message}")
    }
}


package com.example.applicationsop.Api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.models.Approval
import com.example.applicationsop.core.ApiConfig
import com.example.applicationsop.models.ApprovalResponse
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

// Initialize HttpClient with JSON support
val GetApproval = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })  // Handle JSON with Kotlinx serialization
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}
suspend fun fetchApprovalList(persetujuanId: String? = null): List<Approval> {
    return try {
        val url = buildString {
            append("${ApiConfig.BASE_URL}approval?")
            if (persetujuanId != null) append("persetujuanId=$persetujuanId")
        }

        // Make the GET request with the built URL
        val response: HttpResponse = GetApproval.get(url) {
            contentType(ContentType.Application.Json)
        }

        if (response.status.value in 200..299) {
            println("Successful response Approval!")
        }

        // Deserialize the response body into ResponseApproval
        val responseApproval: ApprovalResponse = response.body()
        println("Approval List: ${responseApproval.payload}")

        responseApproval.payload  // Return the filtered list
    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        emptyList()  // Return an empty list on error
    }
}


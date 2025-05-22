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
import com.example.applicationsop.models.Users
import com.example.applicationsop.models.ResponseUser
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

// Initialize HttpClient with JSON support
val GetUserClient = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })  // Handle JSON with Kotlinx serialization
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

// Function to fetch user list
suspend fun fetchUserList(context: Context): List<Users> {
    return try {
        val userData = UserUtils.getUserData(context)
        val token = userData["token"]

        val response: HttpResponse = GetUserClient.get("${ApiConfig.BASE_URL}users") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        if (response.status.value in 200..299) {
            println("Successful response Get Users!")
        }

        val responseUser: ResponseUser = response.body()
        println("User List: ${responseUser.payload}")

        responseUser.payload
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}


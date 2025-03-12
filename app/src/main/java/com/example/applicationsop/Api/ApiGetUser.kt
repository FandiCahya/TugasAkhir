package com.example.applicationsop.Api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.core.ApiConfig
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
suspend fun fetchUserList(): List<Users> {
    return try {
        val response: HttpResponse = GetUserClient.get("${ApiConfig.BASE_URL}users") {
            contentType(ContentType.Application.Json)
        }

        if (response.status.value in 200..299) {
            println("Successful response Get Users!")
        }

        // Deserialize the response body into ResponseUser
        val responseUser: ResponseUser = response.body()
        println("User List: ${responseUser.payload}")

        responseUser.payload // Return the list of Users
    } catch (e: Exception) {
        e.printStackTrace()  // Log the exception
        emptyList()  // Return an empty list on error
    }
}

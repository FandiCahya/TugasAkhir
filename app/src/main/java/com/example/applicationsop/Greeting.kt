package com.example.applicationsop

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class Greeting {
    private val client = HttpClient()

    suspend fun greeting(): String {
        val response = client.get("http://127.0.0.1:8000/api/")
        return response.bodyAsText()
    }
}
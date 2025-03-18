package com.example.applicationsop.Api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import com.example.applicationsop.core.ApiConfig
import com.example.applicationsop.models.Pengujian
import com.example.applicationsop.models.ResponsePengujian
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

// Initialize HttpClient with JSON support
val GetPengujian = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })  // Handle JSON with Kotlinx serialization
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}

// Function to fetch pengembangan list
suspend fun fetchPengujianList(pengujian_id: String? = null,user_id: String? = null,disetujui_oleh: String? = null): List<Pengujian> {
    return try {
        val url = buildString {
            append("${ApiConfig.BASE_URL}pengujian?")
            val params = mutableListOf<String>()

            if (!pengujian_id.isNullOrEmpty()) params.add("pengujian_id=$pengujian_id")
            if (!user_id.isNullOrEmpty()) params.add("user_id=$user_id")
            if (!disetujui_oleh.isNullOrEmpty()) params.add("disetujui_oleh=$disetujui_oleh")

            append(params.joinToString("&"))
        }

        println("Fetching URL: $url") // Debugging

        val response: HttpResponse = GetPengajuan.get(url) {
            contentType(ContentType.Application.Json)
        }

        if (response.status.value in 200..299) {
            println("Successful response Get Pengujian!")
        }

        val responsePengujian: ResponsePengujian = response.body()
        println("Pengujian List: ${responsePengujian.payload}")

        responsePengujian.payload
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}




package com.example.applicationsop.core

object ApiConfig {
    const val BASE_URL = "http://${Config.IP_ADDRESS}:${Config.PORT}/api/"
}

object UrlConfig {
    const val BASE_URL = "http://${Config.IP_ADDRESS}:${Config.PORT}/"
}

object urlSignature{
    const val BASE_URL = "http://${Config.IP_ADDRESS}:${Config.PORT}/storage/"
}
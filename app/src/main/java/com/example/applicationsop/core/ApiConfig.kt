package com.example.applicationsop.core

// File: ApiConfig.kt

//Pakai Port
object ApiConfig {
    const val BASE_URL = "http://${Config.IP_ADDRESS}:${Config.PORT}/api/"
}

object UrlConfig {
    const val BASE_URL = "http://${Config.IP_ADDRESS}:${Config.PORT}/"
}

object urlSignature{
    const val BASE_URL = "http://${Config.IP_ADDRESS}:${Config.PORT}/storage/"
}

////Tanpa Port
//object ApiConfig {
//    const val BASE_URL = "http://${Config.IP_ADDRESS}/api/"
//}
//
//object UrlConfig {
//    const val BASE_URL = "http://${Config.IP_ADDRESS}/"
//}
//
//object urlSignature{
//    const val BASE_URL = "http://${Config.IP_ADDRESS}/storage/"
//}
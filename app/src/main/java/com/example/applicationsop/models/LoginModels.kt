package com.example.applicationsop.models
import kotlinx.serialization.Serializable
// File: LoginModels.kt
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
@Serializable
data class LoginResponse(
    val message: String,
    val token: String?,
    val user: User
)

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val devisi: String,
    val role: String
)

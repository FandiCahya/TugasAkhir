package com.example.applicationsop.models

// File: LoginModels.kt

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val token: String?,
    val user: User
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val devisi: String,
    val role: String
)

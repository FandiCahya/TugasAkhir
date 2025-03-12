package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class Users(
    val id: String,
    val name: String,
    val email: String,
    val email_verified_at: String? = null,
    val devisi: String,
    val foto_profile: String? = null,
    val role: String,
    val signature: String? = null,
    val created_at: String,
    val updated_at: String
)

@Serializable
data class ResponseUser(
    val success: Boolean,
    val payload: List<Users>
)
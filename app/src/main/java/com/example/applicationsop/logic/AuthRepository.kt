package com.example.applicationsop.logic

import com.example.applicationsop.Api.loginUser
import com.example.applicationsop.models.LoginResponse
// File: AuthRepository.kt

class AuthRepository {

    suspend fun login(email: String, password: String): LoginResponse? {
        return loginUser(email, password)  // Memanggil fungsi loginUser yang ada di ApiClient.kt
    }
}

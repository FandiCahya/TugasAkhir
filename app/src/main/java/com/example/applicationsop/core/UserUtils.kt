package com.example.applicationsop.core

import android.app.Activity
import android.content.Context

object UserUtils {
    fun getUserData(context: Context): Map<String, Any?> {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)
        val userId = sharedPreferences.getString("USER_ID", null)
        val role = sharedPreferences.getString("ROLE", null)
        val name = sharedPreferences.getString("NAME", null)
        val email = sharedPreferences.getString("EMAIL", null)
        val devisi = sharedPreferences.getString("DEVISI", null)
        val isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false)

        return mapOf(
            "token" to token,
            "userId" to userId,
            "role" to role,
            "name" to name,
            "email" to email,
            "devisi" to devisi,
            "isLoggedIn" to isLoggedIn
        )
    }
}
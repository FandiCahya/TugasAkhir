package com.example.applicationsop.core

import android.app.Activity
import android.content.Context
import org.json.JSONObject

fun saveUserData(
    context: Context,
    token: String,
    userId: String,
    role: String,
    name: String,
    email: String,
    devisi: String
) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("TOKEN", token)  // Simpan token
    editor.putString("USER_ID", userId) // Simpan userId
    editor.putString("ROLE", role) // Simpan role
    editor.putString("NAME", name) // Simpan name
    editor.putString("EMAIL", email) // Simpan email
    editor.putString("DEVISI", devisi) // Simpan devisi
    editor.putBoolean("IS_LOGGED_IN", true)
    editor.apply() // Commit perubahan
}




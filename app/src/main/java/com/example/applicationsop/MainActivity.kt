package com.example.applicationsop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.applicationsop.core.UserUtils
import com.example.applicationsop.navigation.MainApp
import com.example.applicationsop.ui.theme.ApplicationSOPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentToken = intent.getStringExtra("token")
        val intentUserId = intent.getStringExtra("userId")
        val intentRole = intent.getStringExtra("role")
        val intentName = intent.getStringExtra("name")
        val intentEmail = intent.getStringExtra("email")
        val intentDevisi = intent.getStringExtra("devisi")

        val userData = UserUtils.getUserData(this)
        val fallbackToken = userData["token"] as? String
        val fallbackRole = userData["role"] as? String
        val fallbackUserId = userData["userId"] as? String
        val fallbackName = userData["name"] as? String
        val fallbackEmail = userData["email"] as? String
        val fallbackDevisi = userData["devisi"] as? String

        val token = intentToken ?: fallbackToken
        val userId = intentUserId ?: fallbackUserId
        val role = intentRole ?: fallbackRole
        val name = intentName ?: fallbackName
        val email = intentEmail ?: fallbackEmail
        val devisi = intentDevisi ?: fallbackDevisi


        setContent {
            ApplicationSOPTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    MainApp(
                        navController = navController,
                        token = token,
                        userId = userId,
                        role = role,
                        name = name,
                        email = email,
                        devisi = devisi
                    )
                }
            }
        }
    }
}


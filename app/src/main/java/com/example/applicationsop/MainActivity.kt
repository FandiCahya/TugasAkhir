package com.example.applicationsop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.applicationsop.navigation.MainApp
import com.example.applicationsop.navigation.MainApp
import com.example.applicationsop.ui.theme.ApplicationSOPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApplicationSOPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController() // Inisialisasi NavController
//                    MainApp() // Panggil AppNavigation dengan navController

                    // Retrieve user data from Intent
                    val token = intent.getStringExtra("token")
                    val userId = intent.getStringExtra("userId")
                    val role = intent.getStringExtra("role")
                    val name = intent.getStringExtra("name")
                    val email = intent.getStringExtra("email")
                    val devisi = intent.getStringExtra("devisi")

                    println("Token: $token")

                    // Check if token exists
                    if (token != null) {
                        // Pass data to MainApp
                        MainApp(
                            navController = navController,
                            token = token,
                            userId = userId,
                            role = role,
                            name = name,
                            email = email,
                            devisi = devisi
                        )
                    } else {
                        // If no token, navigate to login
                        MainApp(
                            navController = navController,
                            token = null,
                            userId = null,
                            role = null,
                            name = null,
                            email = null,
                            devisi = null
                        )
                    }
                }
            }
        }
    }
}

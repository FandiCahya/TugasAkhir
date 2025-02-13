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
                    MainApp() // Panggil AppNavigation dengan navController
                }
            }
        }
    }
}

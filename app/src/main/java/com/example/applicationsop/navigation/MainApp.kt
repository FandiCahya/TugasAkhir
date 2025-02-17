package com.example.applicationsop.navigation

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.applicationsop.loginScreen.LoginScreen
import com.example.applicationsop.presentation.screen.ListPengajuanScreen
import com.example.applicationsop.presentation.screen.ProfileScreen
import com.example.applicationsop.presentation.screen.user.FormUsulanScreen
import com.example.applicationsop.presentation.screen.user.HomeUserScreen
import com.example.applicationsop.presentation.screen.user.ListPengembanganScreen

@Composable
fun MainApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login" // Set rute awal untuk login
    ) {
        // Rute untuk LoginScreen
        composable("login") {
            LoginScreen(navController = navController) // Pass navController ke LoginScreen
        }

        // Rute untuk HomeUserScreen
        composable("home") {
            HomeUserScreen(navController = navController) // Pass navController ke HomeUserScreen
        }

        // Halaman FormUsulanScreen
        composable("form_usulan") {
            FormUsulanScreen(navController = navController) // Pastikan ini sesuai dengan nama layar
        }

        // Profile
        composable("profile") {
            ProfileScreen(navController = navController)
        }

        // List Pengajuan
        composable("list_pengajuan") {
            ListPengajuanScreen(navController = navController)
        }

        // Pengembangaan user
        composable("pengembangan") {
            ListPengembanganScreen(navController = navController)
        }
    }
}

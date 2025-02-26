package com.example.applicationsop.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.applicationsop.loginScreen.LoginScreen
import com.example.applicationsop.presentation.screen.pengguna.ListPengajuanScreen
import com.example.applicationsop.presentation.screen.ProfileScreen1
import com.example.applicationsop.presentation.screen.admin.form.FormPengujianAdmin
import com.example.applicationsop.presentation.screen.admin.HomeAdminScreen
import com.example.applicationsop.presentation.screen.admin.ListPengajuanScreenAdmin
import com.example.applicationsop.presentation.screen.admin.ListPengembanganAdminScreen
import com.example.applicationsop.presentation.screen.admin.ListPengujianScreenAdmin
import com.example.applicationsop.presentation.screen.admin.form.ScheduleForm
import com.example.applicationsop.presentation.screen.pengguna.FormUsulanScreen
import com.example.applicationsop.presentation.screen.pengguna.HomeUserScreen
import com.example.applicationsop.presentation.screen.pengguna.ListPengembanganScreen

@RequiresApi(Build.VERSION_CODES.O)
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


        // Halaman FormUsulanScreen
        composable("form_usulan") {
            FormUsulanScreen(navController = navController) // Pastikan ini sesuai dengan nama layar
        }

        // Profile
        composable("profile?token={token}&userId={userId}&role={role}&name={name}&email={email}&devisi={devisi}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            val userId = backStackEntry.arguments?.getString("userId")
            val role = backStackEntry.arguments?.getString("role")
            val name = backStackEntry.arguments?.getString("name")
            val email = backStackEntry.arguments?.getString("email")
            val devisi = backStackEntry.arguments?.getString("devisi")

            ProfileScreen1(
                navController = navController,
                token = token,
                userId = userId,
                role = role,
                name = name,
                email = email,
                devisi = devisi
            )
        }


        // List Pengajuan
        composable("list_pengajuanUser") {
            ListPengajuanScreen(navController = navController)
        }

        // Pengembangaan user
        composable("pengembanganUser") {
            ListPengembanganScreen(navController = navController)
        }

        //User Navigation
        // Rute untuk HomeUserScreen
        composable("homeUser/{token}/{userId}/{role}/{name}/{email}/{devisi}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            val userId = backStackEntry.arguments?.getString("userId")
            val role = backStackEntry.arguments?.getString("role")
            val name = backStackEntry.arguments?.getString("name")
            val email = backStackEntry.arguments?.getString("email")
            val devisi = backStackEntry.arguments?.getString("devisi")
            HomeUserScreen(navController = navController, token = token, userId = userId, role = role, name = name, email = email, devisi = devisi)
        }

        // Admin Navigation
        // Rute untuk HomeAdminScreen dengan parameter
        composable("homeAdmin/{token}/{userId}/{role}/{name}/{email}/{devisi}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            val userId = backStackEntry.arguments?.getString("userId")
            val role = backStackEntry.arguments?.getString("role")
            val name = backStackEntry.arguments?.getString("name")
            val email = backStackEntry.arguments?.getString("email")
            val devisi = backStackEntry.arguments?.getString("devisi")
            HomeAdminScreen(navController = navController, token = token, userId = userId, role = role, name = name, email = email, devisi = devisi)
        }

        // List Pengajuan
        composable("list_pengajuanAdmin") {
            ListPengajuanScreenAdmin(navController = navController )
        }

        // Pengembangaan user
        composable("pengembanganAdmin") {
            ListPengembanganAdminScreen(navController = navController)
        }

        // Pengujian user
        composable("pengujianAdmin") {
            ListPengujianScreenAdmin(navController = navController)
        }

        // Halaman FormUsulanScreen
        composable("formPengujian") {
            FormPengujianAdmin(navController = navController) // Pastikan ini sesuai dengan nama layar
        }

        // Direct tambah jadwal pengembangan
        composable("addSchedule") {
            ScheduleForm(
                navController = navController,
                onSave = { scheduleItem ->
                    // Misalnya, kamu menyimpan scheduleItem ke database atau state management
                    navController.popBackStack() // Kembali ke halaman sebelumnya setelah menyimpan
                }
            )
        }
    }
}

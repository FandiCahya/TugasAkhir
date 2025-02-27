package com.example.applicationsop.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.applicationsop.loginScreen.LoginScreen
import com.example.applicationsop.presentation.screen.ProfileScreen1
import com.example.applicationsop.presentation.screen.admin.form.FormPengujianAdmin
import com.example.applicationsop.presentation.screen.admin.HomeAdminScreen
import com.example.applicationsop.presentation.screen.admin.list.ListPengajuanScreenAdmin1
import com.example.applicationsop.presentation.screen.admin.list.ListPengajuanScreenAdmin2
import com.example.applicationsop.presentation.screen.admin.list.ListPengajuanScreenAdmin3
import com.example.applicationsop.presentation.screen.admin.ListPengembanganAdminScreen
import com.example.applicationsop.presentation.screen.admin.ListPengujianScreenAdmin
import com.example.applicationsop.presentation.screen.admin.form.ScheduleForm
import com.example.applicationsop.presentation.screen.pemohon.form.FormUsulanScreen
import com.example.applicationsop.presentation.screen.pemohon.HomeUserScreen
import com.example.applicationsop.presentation.screen.pemohon.ListPengembanganScreen
import com.example.applicationsop.presentation.screen.pemohon.list.ListPengajuanScreenButuhKonfirmasi
import com.example.applicationsop.presentation.screen.pemohon.list.ListPengajuanScreenDiterima
import com.example.applicationsop.presentation.screen.pemohon.list.ListPengajuanScreenDitolak

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


        // Halaman FormUsulanScreen User
        composable("form_usulan?userId={userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            FormUsulanScreen(navController = navController, userId = userId)
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
        composable("listUsulan1") {
            ListPengajuanScreenButuhKonfirmasi(navController = navController )
        }

        // List Pengajuan
        composable("listUsulan2") {
            ListPengajuanScreenDiterima(navController = navController )
        }

        // List Pengajuan
        composable("listUsulan3") {
            ListPengajuanScreenDitolak(navController = navController )
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
        composable("list_pengajuanAdmin1") {
            ListPengajuanScreenAdmin1(navController = navController )
        }

        // List Pengajuan
        composable("list_pengajuanAdmin2") {
            ListPengajuanScreenAdmin2(navController = navController )
        }

        // List Pengajuan
        composable("list_pengajuanAdmin3") {
            ListPengajuanScreenAdmin3(navController = navController )
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

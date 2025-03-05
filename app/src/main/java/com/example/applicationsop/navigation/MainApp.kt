package com.example.applicationsop.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
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
import com.example.applicationsop.presentation.screen.pemohon.form.FormEditUsulan
import com.example.applicationsop.presentation.screen.pemohon.list.ListPengajuanScreenButuhKonfirmasi
import com.example.applicationsop.presentation.screen.pemohon.list.ListPengajuanScreenDiterima
import com.example.applicationsop.presentation.screen.pemohon.list.ListPengajuanScreenDitolak

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp(navController: NavController, token: String?, userId: String?, role: String?, name: String?, email: String?, devisi: String?) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = if (token != null) "home" else "login") {

        composable("login") {
            LoginScreen(navController = navController) // Pass navController to LoginScreen
        }
        composable("home") {
            if (token != null) {
                // Navigate based on the role
                if (role == "admin") {
                    HomeAdminScreen(
                        navController = navController,
                        token = token,
                        userId = userId,
                        role = role,
                        name = name,
                        email = email,
                        devisi = devisi
                    )
                } else {
                    HomeUserScreen(
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



        // Halaman FormUsulanScreen User
        composable("form_usulan?userId={userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            FormUsulanScreen(navController = navController, userId = userId)
        }

        // Pengembangaan user
        composable("form_edit_usulan?id={id}&hariTanggal={hariTanggal}&namaSistem={namaSistem}&jenisSistem={jenisSistem}&rencanaAnggaran={rencanaAnggaran}&masalahSistem={masalahSistem}&outputHasil={outputHasil}&status={status}&alasan={alasan}") {  backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val namaSistem = backStackEntry.arguments?.getString("namaSistem")
            val hariTanggal = backStackEntry.arguments?.getString("hariTanggal")
            val jenisSistem = backStackEntry.arguments?.getString("jenisSistem")
            val rencanaAnggaran = backStackEntry.arguments?.getString("rencanaAnggaran")
            val masalahSistem = backStackEntry.arguments?.getString("masalahSistem")
            val outputHasil = backStackEntry.arguments?.getString("outputHasil")
            val status = backStackEntry.arguments?.getString("status")
            val alasan = backStackEntry.arguments?.getString("alasan")
            FormEditUsulan(navController = navController, id = id, nama_Sistem = namaSistem, hari_Tanggal = hariTanggal, jenis_Sistem = jenisSistem,rencana_Anggaran = rencanaAnggaran,masalah_Sistem=masalahSistem,output_Hasil=outputHasil,status=status, alasan_penolakan = alasan)
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
        composable("listUsulan1?role={role}&devisi={devisi}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role")
            val devisi = backStackEntry.arguments?.getString("devisi")
            ListPengajuanScreenButuhKonfirmasi(navController = navController,role = role, devisi = devisi )
        }

        // List Pengajuan
        composable("listUsulan2?role={role}&devisi={devisi}") {backStackEntry ->
            val role = backStackEntry.arguments?.getString("role")
            val devisi = backStackEntry.arguments?.getString("devisi")
            ListPengajuanScreenDiterima(navController = navController,role = role, devisi = devisi )
        }

        // List Pengajuan
        composable("listUsulan3?role={role}&devisi={devisi}") {backStackEntry ->
            val role = backStackEntry.arguments?.getString("role")
            val devisi = backStackEntry.arguments?.getString("devisi")
            ListPengajuanScreenDitolak(navController = navController,role = role, devisi = devisi )
        }

        // Pengembangaan user
        composable("pengembanganUser") {
            ListPengembanganScreen(navController = navController)
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

        // Halaman Form Pengujian
        composable("formPengujian?id={id}&taskName={taskName}") {backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val taskName = backStackEntry.arguments?.getString("taskName")
            FormPengujianAdmin(
                navController = navController,
                idPengembangan = id,
                namaSistem = taskName, // Pastikan Anda mengirimkan parameter yang diperlukan
            )
        }

        // Direct tambah jadwal pengembangan
        composable("addSchedule?id={id}&namaSistem={namaSistem}") {backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val namaSistem = backStackEntry.arguments?.getString("namaSistem")
            ScheduleForm(
                navController = navController, id = id,namaSistem = namaSistem,
                onSave = { scheduleItem ->
                    // Misalnya, kamu menyimpan scheduleItem ke database atau state management
                    navController.popBackStack() // Kembali ke halaman sebelumnya setelah menyimpan
                }
            )
        }
    }
}

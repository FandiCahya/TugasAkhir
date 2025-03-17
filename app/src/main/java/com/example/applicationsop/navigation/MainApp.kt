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
import com.example.applicationsop.presentation.screen.admin.DetailApproval
import com.example.applicationsop.presentation.screen.admin.DetailPengujianAdmin
import com.example.applicationsop.presentation.screen.admin.HistoryAdmin
import com.example.applicationsop.presentation.screen.admin.form.FormPengujianAdmin
import com.example.applicationsop.presentation.screen.admin.HomeAdminScreen
import com.example.applicationsop.presentation.screen.admin.list.ListPengajuanScreenAdmin1
import com.example.applicationsop.presentation.screen.admin.list.ListPengajuanScreenAdmin2
import com.example.applicationsop.presentation.screen.admin.list.ListPengajuanScreenAdmin3
import com.example.applicationsop.presentation.screen.admin.ListPengembanganAdminScreen
import com.example.applicationsop.presentation.screen.admin.ListPengujianScreenAdmin
import com.example.applicationsop.presentation.screen.admin.form.ScheduleForm
import com.example.applicationsop.presentation.screen.kacab.list.ListPengajuanScreenKacab1
import com.example.applicationsop.presentation.screen.kacab.list.ListPengajuanScreenKacab2
import com.example.applicationsop.presentation.screen.kacab.list.ListPengajuanScreenKacab3
import com.example.applicationsop.presentation.screen.kacab.HomeKacabScreen
import com.example.applicationsop.presentation.screen.kacab.ListPengembanganKacab
import com.example.applicationsop.presentation.screen.pemohon.HistoryUser
import com.example.applicationsop.presentation.screen.pemohon.form.FormUsulanScreen
import com.example.applicationsop.presentation.screen.pemohon.HomeUserScreen
import com.example.applicationsop.presentation.screen.pemohon.ListPengembanganScreen
import com.example.applicationsop.presentation.screen.pemohon.ListPengujianScreenUser
import com.example.applicationsop.presentation.screen.pemohon.form.FormEditUsulan
import com.example.applicationsop.presentation.screen.pemohon.list.ListPengajuanScreenButuhKonfirmasi
import com.example.applicationsop.presentation.screen.pemohon.list.ListPengajuanScreenDiterima
import com.example.applicationsop.presentation.screen.pemohon.list.ListPengajuanScreenDitolak
import com.example.applicationsop.presentation.screen.qmr.HomeQmrScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp(
    navController: NavController,
    token: String?,
    userId: String?,
    role: String?,
    name: String?,
    email: String?,
    devisi: String?
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (token != null) "home" else "login"
    ) {

        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("home") {
            if (token != null) {
                when (role) {
                    "admin" -> {
                        HomeAdminScreen(
                            navController = navController,
                            token = token,
                            userId = userId,
                            role = role,
                            name = name,
                            email = email,
                            devisi = devisi
                        )
                    }

                    "kepalacabang" -> {
                        // Kacab specific screen
                        HomeKacabScreen(
                            navController = navController,
                            token = token,
                            userId = userId,
                            role = role,
                            name = name,
                            email = email,
                            devisi = devisi
                        )
                    }

                    "qmr" -> {
                        // Qmr specific screen
                        HomeQmrScreen(
                            navController = navController,
                            token = token,
                            userId = userId,
                            role = role,
                            name = name,
                            email = email,
                            devisi = devisi
                        )
                    }

                    else -> {
                        // Default user screen if the role doesn't match above
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
        }

        //User Navigation
        composable("homeUser/{token}/{userId}/{role}/{name}/{email}/{devisi}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            val userId = backStackEntry.arguments?.getString("userId")
            val role = backStackEntry.arguments?.getString("role")
            val name = backStackEntry.arguments?.getString("name")
            val email = backStackEntry.arguments?.getString("email")
            val devisi = backStackEntry.arguments?.getString("devisi")
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

        // Admin Navigation
        composable("homeAdmin/{token}/{userId}/{role}/{name}/{email}/{devisi}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            val userId = backStackEntry.arguments?.getString("userId")
            val role = backStackEntry.arguments?.getString("role")
            val name = backStackEntry.arguments?.getString("name")
            val email = backStackEntry.arguments?.getString("email")
            val devisi = backStackEntry.arguments?.getString("devisi")
            HomeAdminScreen(
                navController = navController,
                token = token,
                userId = userId,
                role = role,
                name = name,
                email = email,
                devisi = devisi
            )
        }

        // Kacab Navigation
        composable("homeKacab/{token}/{userId}/{role}/{name}/{email}/{devisi}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            val userId = backStackEntry.arguments?.getString("userId")
            val role = backStackEntry.arguments?.getString("role")
            val name = backStackEntry.arguments?.getString("name")
            val email = backStackEntry.arguments?.getString("email")
            val devisi = backStackEntry.arguments?.getString("devisi")
            HomeKacabScreen(
                navController = navController,
                token = token,
                userId = userId,
                role = role,
                name = name,
                email = email,
                devisi = devisi
            )
        }

        // Qmr Navigation
        composable("homeQmr/{token}/{userId}/{role}/{name}/{email}/{devisi}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            val userId = backStackEntry.arguments?.getString("userId")
            val role = backStackEntry.arguments?.getString("role")
            val name = backStackEntry.arguments?.getString("name")
            val email = backStackEntry.arguments?.getString("email")
            val devisi = backStackEntry.arguments?.getString("devisi")
            HomeQmrScreen(
                navController = navController,
                token = token,
                userId = userId,
                role = role,
                name = name,
                email = email,
                devisi = devisi
            )
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

        ////////////////////////// User ///////////////////////

        // Halaman FormUsulanScreen User
        composable("form_usulan?userId={userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            FormUsulanScreen(navController = navController, userId = userId)
        }

        // Pengembangaan user
        composable("form_edit_usulan?id={id}&hariTanggal={hariTanggal}&namaSistem={namaSistem}&jenisSistem={jenisSistem}&rencanaAnggaran={rencanaAnggaran}&masalahSistem={masalahSistem}&outputHasil={outputHasil}&status={status}&alasan={alasan}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val namaSistem = backStackEntry.arguments?.getString("namaSistem")
            val hariTanggal = backStackEntry.arguments?.getString("hariTanggal")
            val jenisSistem = backStackEntry.arguments?.getString("jenisSistem")
            val rencanaAnggaran = backStackEntry.arguments?.getString("rencanaAnggaran")
            val masalahSistem = backStackEntry.arguments?.getString("masalahSistem")
            val outputHasil = backStackEntry.arguments?.getString("outputHasil")
            val status = backStackEntry.arguments?.getString("status")
            val alasan = backStackEntry.arguments?.getString("alasan")
            FormEditUsulan(
                navController = navController,
                id = id,
                nama_Sistem = namaSistem,
                hari_Tanggal = hariTanggal,
                jenis_Sistem = jenisSistem,
                rencana_Anggaran = rencanaAnggaran,
                masalah_Sistem = masalahSistem,
                output_Hasil = outputHasil,
                status = status,
                alasan_penolakan = alasan
            )
        }

        // List Pengajuan
        composable("listUsulan1?role={role}&devisi={devisi}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role")
            val devisi = backStackEntry.arguments?.getString("devisi")
            ListPengajuanScreenButuhKonfirmasi(
                navController = navController,
                role = role,
                devisi = devisi
            )
        }

        // List Pengajuan
        composable("listUsulan2?role={role}&devisi={devisi}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role")
            val devisi = backStackEntry.arguments?.getString("devisi")
            ListPengajuanScreenDiterima(navController = navController, role = role, devisi = devisi)
        }

        // List Pengajuan
        composable("listUsulan3?role={role}&devisi={devisi}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role")
            val devisi = backStackEntry.arguments?.getString("devisi")
            ListPengajuanScreenDitolak(navController = navController, role = role, devisi = devisi)
        }

        // Pengembangaan user
        composable("pengembanganUser") {
            ListPengembanganScreen(navController = navController)
        }

        // Pengujian User
        composable("pengujianUser") {
            ListPengujianScreenUser(navController = navController)
        }

        // History User
        composable("historyUser") {
            HistoryUser(navController = navController)
        }

        ////////////////////////// ADMIN ///////////////////////

        // List Pengajuan Admin
        composable("list_pengajuanAdmin1") {
            ListPengajuanScreenAdmin1(navController = navController)
        }

        // List Pengajuan Admin
        composable("list_pengajuanAdmin2") {
            ListPengajuanScreenAdmin2(navController = navController)
        }

        // List Pengajuan Admin
        composable("list_pengajuanAdmin3") {
            ListPengajuanScreenAdmin3(navController = navController)
        }

        // Pengembangaan Admin
        composable("pengembanganAdmin") {
            ListPengembanganAdminScreen(navController = navController)
        }

        // Pengujian Admin
        composable("pengujianAdmin") {
            ListPengujianScreenAdmin(navController = navController)
        }

        // History Admin
        composable("historyAdmin") {
            HistoryAdmin(navController = navController)
        }

        // Halaman Detail Pengujian
        composable("DetailPengujianAdmin?id={id}&namaSistem={namaSistem}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val namaSistem = backStackEntry.arguments?.getString("namaSistem")
            DetailPengujianAdmin(
                navController = navController,
                idPengujian = id,
                namaSistem = namaSistem,
            )
        }

        composable("detail_approval_screen/{idPengujian}") { backStackEntry ->
            val idPengujian = backStackEntry.arguments?.getString("idPengujian")
            DetailApproval(navController = navController, idPengujian = idPengujian)
        }

        // Halaman Form Pengujian
        composable("formPengujian?id={id}&taskName={taskName}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val taskName = backStackEntry.arguments?.getString("taskName")
            FormPengujianAdmin(
                navController = navController,
                idPengembangan = id,
                namaSistem = taskName,
            )
        }

        // Direct tambah jadwal pengembangan
        composable("addSchedule?id={id}&namaSistem={namaSistem}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val namaSistem = backStackEntry.arguments?.getString("namaSistem")
            ScheduleForm(
                navController = navController, id = id, namaSistem = namaSistem,
                onSave = { scheduleItem ->
                    navController.popBackStack() // Kembali ke halaman sebelumnya setelah menyimpan
                }
            )
        }

        ////////////////////////// Kacab ///////////////////////

        // List Pengajuan Admin
        composable("list_pengajuanKacab1") {
            ListPengajuanScreenKacab1(navController = navController)
        }

        // List Pengajuan Kacab
        composable("list_pengajuanKacab2") {
            ListPengajuanScreenKacab2(navController = navController)
        }

        // List Pengajuan kacab
        composable("list_pengajuanKacab3") {
            ListPengajuanScreenKacab3(navController = navController)
        }
        // Pengembangaan kacab
        composable("pengembanganKacab") {
            ListPengembanganKacab(navController = navController)
        }

    }
}

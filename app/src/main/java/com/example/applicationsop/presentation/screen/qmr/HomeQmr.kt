package com.example.applicationsop.presentation.screen.qmr

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.kuning
import androidx.navigation.NavController
import com.example.applicationsop.presentation.component.ProgressCard
import com.example.applicationsop.presentation.component.ProgressCardRiwayat
import com.example.applicationsop.presentation.component.SectionTitle
import com.example.applicationsop.presentation.component.SubmissionCard
import com.example.applicationsop.presentation.component.header.HeaderHomeQmr

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeQmrScreen(
    navController: NavController,
    token: String?,
    userId: String?,
    role: String?,
    name: String?,
    email: String?,
    devisi: String?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
    ) {
        // Konten utama, termasuk Header, Sections, dan lainnya
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Memberikan ruang bawah agar FAB tidak tertutup
        ) {
            // Header
            HeaderHomeQmr(
                navController = navController,
                adminName = name,
                adminToken = token,
                adminuserId  = userId,
                adminrole = role,
                adminemail = email ,
                admindevisi = devisi
            )


            // Pengajuan Section
            SectionTitle("Pengajuan")
            SubmissionSection(navController = navController)

            Spacer(modifier = Modifier.height(16.dp))

            // Garis tengah
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 25.dp)
            )

            // Progres Section
            SectionTitle("Progres")
            ProgressSection(navController = navController)
        }
    }
}

@Composable
fun SubmissionSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween // Jarak antar kartu
    ) {
        // Card Butuh Konfirmasi (kuning)
        SubmissionCard(
            color = kuning,
            icon = Icons.Filled.Timer,
            onClick = {
                navController.navigate("list_pengajuanKacab1")
            }
        )

        // Card Ditolak (merah)
        SubmissionCard(
            color = abang,
            icon = Icons.Filled.Close,
            onClick = {
                navController.navigate("list_pengajuanKacab2")
            }
        )

        // Card Dikembangkan (hijau)
        SubmissionCard(
            color = ijo,
            icon = Icons.Filled.Verified,
            onClick = {
                navController.navigate("list_pengajuanKacab3")
            }
        )
    }
}


@Composable
fun ProgressSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Memberikan padding horizontal pada ProgressSection
    ) {
        // Menampilkan beberapa ProgressCard
        ProgressCard("Pengembangan Admin", Icons.Filled.Timer, Maroon, count = null,navController)
        ProgressCard("Pengujian Admin", Icons.Filled.History, Maroon, count = null,navController)

        // Garis tengah
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(
                horizontal = 5.dp,
                vertical = 10.dp
            ) // Berikan ruang kiri dan kanan
        )

        // Card Riwayat dengan Icon di bawah
        ProgressCardRiwayat("Riwayat Admin", Icons.Filled.History, Maroon, navController)
    }
}
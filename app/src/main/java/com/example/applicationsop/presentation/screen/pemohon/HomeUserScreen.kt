package com.example.applicationsop.presentation.screen.pemohon

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.ui.zIndex
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.PinkPudar
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.kuning
import androidx.navigation.NavController
import com.example.applicationsop.presentation.component.ProgressCard
import com.example.applicationsop.presentation.component.ProgressCardRiwayat
import com.example.applicationsop.presentation.component.SubmissionCard
import com.example.applicationsop.presentation.component.header.HeaderHomeUser
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeUserScreen(
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
            HeaderHomeUser(
                navController = navController,
                nameUser = name,
                TokenUser = token,
                userIdUser = userId,
                roleUser = role,
                emailUser = email,
                devisiUser = devisi
            )

            // Pengajuan Section
            SectionTitle("Pengajuan")
            SubmissionSection(navController = navController,roleUS = role,devisiUS = devisi)

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

        // Floating Action Button (FAB)
        FloatingActionButton(
            onClick = {
                // Navigate to FormUsulanScreen when FAB is clicked
                navController.navigate("form_usulan?userId=$userId")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Letakkan FAB di bawah kanan
                .padding(30.dp)
                .zIndex(1f), // Pastikan FAB selalu berada di atas
            containerColor = PinkPudar
        ) {
            Icon(
                imageVector = Icons.Filled.Add, // Ikon "Add"
                contentDescription = "Add",
                tint = Putih
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 25.dp, vertical = 8.dp),
        color = Color.Black
    )
}

@Composable
fun SubmissionSection(navController: NavController, roleUS: String?, devisiUS: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween // Mengatur jarak antar kolom
    ) {
        // Card Butuh Konfirmasi (kuning)
        SubmissionCard(
            color = kuning,
            icon = Icons.Filled.Timer,
            onClick = {
                navController.navigate("listUsulan1?role=$roleUS&devisi=$devisiUS")
            }
        )

        // Card Ditolak (merah)
        SubmissionCard(
            color = abang,
            icon = Icons.Filled.Close,
            onClick = {
                navController.navigate("listUsulan3?role=$roleUS&devisi=$devisiUS")
            }
        )

        // Card Dikembangkan (hijau)
        SubmissionCard(
            color = ijo,
            icon = Icons.Filled.Verified,
            onClick = {
                navController.navigate("listUsulan2?role=$roleUS&devisi=$devisiUS")
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
        ProgressCard("Pengembangan User", Icons.Filled.Timer, Maroon, navController)
        ProgressCard("Pengujian User", Icons.Filled.History, Maroon, navController)

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
        ProgressCardRiwayat("Riwayat", Icons.Filled.History, Maroon)
    }
}
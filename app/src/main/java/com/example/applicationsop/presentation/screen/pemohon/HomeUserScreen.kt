package com.example.applicationsop.presentation.screen.pemohon

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.PinkPudar
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.kuning
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.Api.fetchPengembanganSortList
import com.example.applicationsop.Api.fetchPengujianList
import com.example.applicationsop.presentation.component.ProgressCard
import com.example.applicationsop.presentation.component.ProgressCardRiwayat
import com.example.applicationsop.presentation.component.SectionTitle
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
    // State for controlling visibility and offset for FAB
    val isVisible = remember { mutableStateOf(false) }
    val fabOffset = remember { mutableStateOf(1000) } // Initial offset for FAB sliding
    var pendingCount by remember { mutableStateOf(0) }
    var rejectedCount by remember { mutableStateOf(0) }
    var acceptedCount by remember { mutableStateOf(0) }
    var pengembanganCount by remember { mutableStateOf(0) }
    var pengujianCount by remember { mutableStateOf(0) }
    var riwayatCount by remember { mutableStateOf(0) }
    val context = LocalContext.current

    // Trigger visibility change after the composable is first shown
    LaunchedEffect(true) {
        isVisible.value = true
        pendingCount = fetchPengajuanList(context,"pending", role, devisi).size
        rejectedCount = fetchPengajuanList(context, "rejected", role, devisi).size
        acceptedCount = fetchPengajuanList(context, "accepted", role, devisi).size
        pengembanganCount = fetchPengembanganSortList(context, role, devisi,userId).size
        pengujianCount = fetchPengujianList(context, user_id = userId,status_persetujuan = "approved").size
        riwayatCount = fetchPengajuanList(context, "finished").size
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
    ) {
        // Main content (including header, sections, etc.)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // To ensure FAB is not covered
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
            SubmissionSection(
                navController = navController,
                roleUS = role,
                devisiUS = devisi,
                pendingCount=pendingCount,
                rejectedCount=rejectedCount,
                acceptedCount=acceptedCount,
   )

            Spacer(modifier = Modifier.height(16.dp))

            // Garis tengah
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 25.dp)
            )

            // Progres Section
            SectionTitle("Progres")
            ProgressSection(
                navController = navController,
                pengembanganCount=pengembanganCount,
                pengujianCount=pengujianCount,
                riwayatCount = riwayatCount
            )
        }

        // Animated FAB with sliding and fading animation
        AnimatedVisibility(
            visible = isVisible.value,
            enter = slideIn(initialOffset = { IntOffset(0, 1000) }, animationSpec = tween(durationMillis = 300)),
            exit = slideOut(targetOffset = { IntOffset(0, fabOffset.value) }, animationSpec = tween(durationMillis = 300))
        ) {
            AnimatedVisibility(
                visible = isVisible.value,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                FloatingActionButton(
                    onClick = {
                        // Navigate to FormUsulanScreen when FAB is clicked
                        navController.navigate("form_usulan?userId=$userId")
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd) // Position FAB at the bottom right
                        .padding(top = 700.dp, start = 300.dp)
                        .zIndex(1f),
                    containerColor = PinkPudar // FAB background color
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add, // "Add" icon
                        contentDescription = "Add",
                        tint = Putih
                    )
                }
            }
        }
    }
}

@Composable
fun SubmissionSection(navController: NavController, roleUS: String?, devisiUS: String?, pendingCount: Int, rejectedCount: Int, acceptedCount: Int) {
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
            count = pendingCount,
            onClick = {
                navController.navigate("listUsulan1?role=$roleUS&devisi=$devisiUS")
            }
        )

        // Card Ditolak (merah)
        SubmissionCard(
            color = abang,
            icon = Icons.Filled.Close,
            count = rejectedCount,
            onClick = {
                navController.navigate("listUsulan3?role=$roleUS&devisi=$devisiUS")
            }
        )

        // Card Dikembangkan (hijau)
        SubmissionCard(
            color = ijo,
            icon = Icons.Filled.Verified,
            count = acceptedCount,
            onClick = {
                navController.navigate("listUsulan2?role=$roleUS&devisi=$devisiUS")
            }
        )
    }
}

@Composable
fun ProgressSection(navController: NavController, pengembanganCount: Int, pengujianCount: Int, riwayatCount:Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Memberikan padding horizontal pada ProgressSection
    ) {
        // Menampilkan beberapa ProgressCard
        ProgressCard("Pengembangan User", Icons.Filled.Timer, Maroon, count = pengembanganCount,navController)
        ProgressCard("Pengujian User", Icons.Filled.History, Maroon,count = pengujianCount, navController)

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
        ProgressCardRiwayat("Riwayat User", Icons.Filled.History, Maroon, count = riwayatCount, navController)
    }
}
package com.example.applicationsop.presentation.screen.kacab

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
import androidx.compose.runtime.LaunchedEffect // Keep this import, but we'll remove its usage for initial refresh
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.kuning
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.Api.fetchPengembanganList
import com.example.applicationsop.Api.fetchPengujianList
import com.example.applicationsop.presentation.component.ProgressCard
import com.example.applicationsop.presentation.component.ProgressCardRiwayat
import com.example.applicationsop.presentation.component.SectionTitle
import com.example.applicationsop.presentation.component.SubmissionCard
import com.example.applicationsop.presentation.component.header.HeaderHomeKacab

// Imports for Swipe Refresh
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay // Added for optional delay in refresh

// Imports for scrolling
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeKacabScreen(
    navController: NavController,
    token: String?,
    userId: String?,
    role: String?,
    name: String?,
    email: String?,
    devisi: String?
) {
    var pendingCount by remember { mutableStateOf(0) }
    var rejectedCount by remember { mutableStateOf(0) }
    var acceptedCount by remember { mutableStateOf(0) }
    var pengembanganCount by remember { mutableStateOf(0) }
    var pengujianCount by remember { mutableStateOf(0) }
    var riwayatCount by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope() // Get a CoroutineScope

    // State to control the refresh indicator
    var isRefreshing by remember { mutableStateOf(false) }

    // Function to refresh all data
    val refreshAllData: suspend () -> Unit = {
        isRefreshing = true // Activate refresh indicator

        // Fetch data
        pendingCount = fetchPengajuanList(context ,"pending").size
        rejectedCount = fetchPengajuanList(context ,"rejected").size
        acceptedCount = fetchPengajuanList(context ,"accepted").size
        pengembanganCount = fetchPengembanganList(context).size
        pengujianCount = fetchPengujianList(context ,status_persetujuan = "approved").size
        riwayatCount = fetchPengajuanList(context,"finished").size

        // Optional: add a short delay to simulate loading if fetching is too fast
        delay(1000)

        isRefreshing = false // Deactivate refresh indicator after completion
    }

    // --- REMOVE THIS BLOCK to prevent auto-refresh on initial load ---
    /*
    LaunchedEffect(Unit) {
        refreshAllData()
    }
    */
    // --- END REMOVAL ---

    // Wrap the entire content with SwipeRefresh
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing), // State that controls the indicator
        onRefresh = {
            // Launch a coroutine to call the suspend function when the user pulls to refresh
            coroutineScope.launch {
                refreshAllData()
            }
        },
        modifier = Modifier.fillMaxSize()
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
                    .verticalScroll(rememberScrollState()) // **Crucial for pull-to-refresh**
            ) {
                // Header
                HeaderHomeKacab(
                    navController = navController,
                    adminName = name,
                    adminToken = token,
                    adminuserId = userId,
                    adminrole = role,
                    adminemail = email,
                    admindevisi = devisi
                )

                // Pengajuan Section
                SectionTitle("Pengajuan")
                SubmissionSection(
                    navController = navController,
                    pendingCount = pendingCount,
                    rejectedCount = rejectedCount,
                    acceptedCount = acceptedCount
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
                    pengembanganCount = pengembanganCount,
                    pengujianCount = pengujianCount,
                    riwayatCount = riwayatCount
                )
            }
        }
    }
}

@Composable
fun SubmissionSection(navController: NavController, pendingCount: Int, rejectedCount: Int, acceptedCount: Int) {
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
            count = pendingCount,
            onClick = {
                navController.navigate("list_pengajuanKacab1")
            }
        )

        // Card Ditolak (merah)
        SubmissionCard(
            color = abang,
            icon = Icons.Filled.Close,
            count = rejectedCount,
            onClick = {
                navController.navigate("list_pengajuanKacab2")
            }
        )

        // Card Dikembangkan (hijau)
        SubmissionCard(
            color = ijo,
            icon = Icons.Filled.Verified,
            count = acceptedCount,
            onClick = {
                navController.navigate("list_pengajuanKacab3")
            }
        )
    }
}


@Composable
fun ProgressSection(navController: NavController,pengembanganCount: Int,pengujianCount: Int, riwayatCount:Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Memberikan padding horizontal pada ProgressSection
    ) {
        // Menampilkan beberapa ProgressCard
        ProgressCard("Pengembangan Kacab", Icons.Filled.Timer, Maroon, count = pengembanganCount, navController)
        ProgressCard("Pengujian Admin", Icons.Filled.History, Maroon, count = pengujianCount, navController)

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
        ProgressCardRiwayat("Riwayat Admin", Icons.Filled.History, Maroon, count = riwayatCount, navController)
    }
}
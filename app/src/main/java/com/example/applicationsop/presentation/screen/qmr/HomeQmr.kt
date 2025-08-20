package com.example.applicationsop.presentation.screen.qmr

import android.content.Context
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
import com.example.applicationsop.presentation.component.header.HeaderHomeQmr

// Imports for Swipe Refresh
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay // Added for optional delay in refresh

// Imports for scrolling
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Science
import com.example.applicationsop.Api.fetchPengajuanHome
import com.example.applicationsop.helper.Notification.showNotification
import com.example.applicationsop.models.Pengajuan
import com.example.applicationsop.presentation.component.ListPengajuan.PengajuanListItem
import com.example.applicationsop.presentation.component.ListPengajuan.SubmissionSectionKacab
import com.example.applicationsop.presentation.component.chartcard.PengajuanChartCard
import com.example.applicationsop.ui.theme.BiruMuda
import com.example.applicationsop.ui.theme.PinkPudar
import com.example.applicationsop.ui.theme.Purple40
import com.example.applicationsop.ui.theme.birutua

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
    var pendingCount by remember { mutableStateOf(0) }
    var rejectedCount by remember { mutableStateOf(0) }
    var acceptedCount by remember { mutableStateOf(0) }
    var pengembanganCount by remember { mutableStateOf(0) }
    var pengujianCount by remember { mutableStateOf(0) }
    var riwayatCount by remember { mutableStateOf(0)}
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope() // Get a CoroutineScope
    var pengajuanList by remember { mutableStateOf<List<Pengajuan>>(emptyList()) }
    // State to control the refresh indicator
    var isRefreshing by remember { mutableStateOf(false) }

    suspend fun loadDataCounts(appContext: Context) {
        val allPengajuan = fetchPengajuanHome(context, status = null)
        pengajuanList = allPengajuan.sortedByDescending { it.tgl }

        pendingCount = fetchPengajuanList(appContext, "pending").size
        rejectedCount = fetchPengajuanList(appContext, "rejected").size
        acceptedCount = fetchPengajuanList(appContext, "accepted").size
        pengembanganCount = fetchPengembanganList(appContext).size
        pengujianCount = fetchPengujianList(appContext, status_persetujuan = "approved").size
        riwayatCount = fetchPengajuanList(appContext, "finished").size

        val pengujianList = fetchPengajuanList(context, "approval")

        if (pengujianList.isNotEmpty()) {
            showNotification(
                context = context,
                title = "Butuh Persetujuan",
                message = "Terdapat $pengujianCount pengajuan yang belum disetujui"
            )
        }
    }
    // Function to refresh all data
    val refreshAllData: suspend () -> Unit = {
        isRefreshing = true

        loadDataCounts(context)

        delay(100)

        isRefreshing = false
    }

    LaunchedEffect(Unit) {
        loadDataCounts(context)
    }

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
                HeaderHomeQmr(
                    navController = navController,
                    adminName = name,
                    adminToken = token,
                    adminuserId  = userId,
                    adminrole = role,
                    adminemail = email ,
                    admindevisi = devisi
                )

                // Greeting section
                Text(
                    text = "Hello, ${name.orEmpty()} 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    color = birutua,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )
                Text(
                    text = "Let’s see what’s going on today!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                )
                PengajuanChartCard(
                    pending = pendingCount,
                    accepted = acceptedCount,
                    rejected = rejectedCount,
                    pengembangan = pengembanganCount,
                    pengujian = pengujianCount,
                    finished = riwayatCount
                )

                // Pengajuan Section
                SectionTitle("Pengajuan")
                SubmissionSectionKacab(
                    navController = navController,
                    pendingCount = pendingCount,
                    rejectedCount = rejectedCount,
                    acceptedCount = acceptedCount,
                    pengembanganCount = pengembanganCount,
                    pengujianCount = pengujianCount,
                    finishedCount = riwayatCount
                )
                Spacer(modifier = Modifier.height(5.dp))
                SectionTitle("List Pengajuan")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 400.dp) // Ubah tinggi sesuai kebutuhan
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    pengajuanList.forEach { item ->
                        val status = item.status ?: "-"
                        val icon = when (status) {
                            "pending" -> Icons.Default.Schedule
                            "accepted" -> Icons.Default.CheckCircle
                            "rejected" -> Icons.Default.Cancel
                            "developing" -> Icons.Default.Build
                            "testing" -> Icons.Default.Science
                            "finished" -> Icons.Default.DoneAll
                            else -> Icons.Default.Help
                        }
                        PengajuanListItem(
                            title = item.nama_sistem,
                            status = status,
                            time = item.tgl ?: "-",
                            typeColor = when (item.status) {
                                "pending" -> kuning
                                "accepted" -> ijo
                                "rejected" -> abang
                                "developing" -> PinkPudar
                                "testing" -> BiruMuda
                                "finished" -> Purple40
                                else -> BiruMuda
                            },
                            icon = icon,
                            pengajuName = item.user.name ?: "Tidak diketahui",
                            onClick = {
                                when (status) {
                                    "pending", "accepted", "rejected" -> {
                                        navController.navigate("detail_usulan_kacab?id=${item.id}")
                                    }
                                    "developing" -> navController.navigate("detail_pengembangan_kacab?id=${item.id}")
                                    "testing","approval" -> navController.navigate("detail_pengujian_all?id=${item.id}")
                                    "finished" -> navController.navigate("detail_laporan?id=${item.id}")
                                    else -> {} // atau tampilkan toast/snackbar
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

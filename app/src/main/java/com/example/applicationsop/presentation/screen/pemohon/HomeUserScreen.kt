package com.example.applicationsop.presentation.screen.pemohon

import SubmissionSection
import android.content.Context
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
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.example.applicationsop.ui.theme.PinkPudar
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.ui.theme.kuning
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.Api.fetchPengembanganSortList
import com.example.applicationsop.Api.fetchPengujianList
import com.example.applicationsop.presentation.component.SectionTitle
import com.example.applicationsop.presentation.component.header.HeaderHomeUser
import androidx.compose.material.icons.filled.*
// Imports for Swipe Refresh
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay // Added for optional delay in refresh

// Imports for scrolling
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AccessTime
import com.example.applicationsop.Api.fetchPengajuanHome
import com.example.applicationsop.models.Pengajuan
import com.example.applicationsop.presentation.component.ListPengajuan.PengajuanListItem
import com.example.applicationsop.presentation.component.chartcard.PengajuanChartCard
import com.example.applicationsop.ui.theme.BiruMuda
import com.example.applicationsop.ui.theme.Purple40
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.birutua
import com.example.applicationsop.ui.theme.ijo


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
    val isVisible = remember { mutableStateOf(true) }
    val fabOffset = remember { mutableStateOf(1000) }
    var pendingCount by remember { mutableStateOf(0) }
    var rejectedCount by remember { mutableStateOf(0) }
    var acceptedCount by remember { mutableStateOf(0) }
    var pengembanganCount by remember { mutableStateOf(0) }
    var pengujianCount by remember { mutableStateOf(0) }
    var riwayatCount by remember { mutableStateOf(0) }
    var pengajuanList by remember { mutableStateOf<List<Pengajuan>>(emptyList()) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope() // Get a CoroutineScope
    println("User ID home screen: $userId")
    // State to control the refresh indicator
    var isRefreshing by remember { mutableStateOf(false) }

    suspend fun loadDataCounts(appContext: Context) {
        val allPengajuan = fetchPengajuanHome(context, status = null, role, devisi, userId)
        pengajuanList = allPengajuan.sortedByDescending { it.tgl }
        pendingCount = fetchPengajuanList(context, "pending", role, devisi).size
        rejectedCount = fetchPengajuanList(context, "rejected", role, devisi).size
        acceptedCount = fetchPengajuanList(context, "accepted", role, devisi).size
        pengembanganCount = fetchPengembanganSortList(context, role, devisi, userId).size
        pengujianCount = fetchPengujianList(context, user_id = userId, status_persetujuan = "approved").size
        riwayatCount = fetchPengajuanList(context, "finished", role, devisi).size
    }
    println("Pengajuan List User: $pengajuanList")

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
            // Main content (including header, sections, etc.)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp) // To ensure FAB is not covered
                    .verticalScroll(rememberScrollState()) // **Crucial for pull-to-refresh**
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
                SubmissionSection(
                    navController = navController,
                    roleUS = role,
                    devisiUS = devisi,
                    pendingCount = pendingCount,
                    rejectedCount = rejectedCount,
                    acceptedCount = acceptedCount,
                    pengembanganCount = pengembanganCount,
                    pengujianCount = pengujianCount,
                    finishedCount = riwayatCount
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Garis tengah
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 25.dp)
                )
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
                                        navController.navigate("detail_usulan?id=${item.id}&userId=${userId}&role=${role}&devisi=${devisi}")
                                    }
                                    "developing" -> navController.navigate("detail_pengembangan?id=${item.id}&userId=$userId&role=$role&devisi=$devisi")
                                    "testing","approval" -> navController.navigate("detail_pengujian?id=${item.id}&userId=$userId")
                                    "finished" -> navController.navigate("detail_dokumentasi?id=${item.id}")
                                    else -> {} // atau tampilkan toast/snackbar
                                }
                            }
                        )
                    }
                }
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
                            navController.navigate("form_usulan?userId=$userId")
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd) // Position FAB at the bottom right
                            .padding(top = 752.dp, start = 165.dp)
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
}

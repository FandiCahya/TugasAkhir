package com.example.applicationsop.presentation.screen.admin

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
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.kuning
import androidx.navigation.NavController
import com.example.applicationsop.presentation.component.SectionTitle
import com.example.applicationsop.presentation.component.header.HeaderHomeAdmin
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.Api.fetchPengembanganList
import com.example.applicationsop.Api.fetchPengujianList
import android.content.Context
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.applicationsop.presentation.component.ListPengajuan.SubmissionSectionAdmin
import com.example.applicationsop.presentation.component.chartcard.PengajuanChartCard
import com.example.applicationsop.ui.theme.BiruMuda
import com.example.applicationsop.ui.theme.PinkPudar
import com.example.applicationsop.ui.theme.Purple40
import com.example.applicationsop.ui.theme.birutua

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeAdminScreen(
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

    var pengajuanList by remember { mutableStateOf<List<Pengajuan>>(emptyList()) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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
        val pendingList = fetchPengajuanList(context, "pending")
        val acceptedList = fetchPengajuanList(context, "accepted")

        if (pengujianList.isNotEmpty()) {
            showNotification(
                context = context,
                title = "Butuh Persetujuan",
                message = "Terdapat $pengujianCount pengajuan yang belum disetujui"
            )
        }
        if (pendingList.isNotEmpty()) {
            showNotification(
                context = context,
                title = "Butuh Konfirmasi",
                message = "Terdapat $pendingCount pengajuan yang belum dikonfirmasi"
            )
        }
        if (acceptedList.isNotEmpty()) {
            showNotification(
                context = context,
                title = "Butuh Persetujuan",
                message = "Terdapat $acceptedCount pengajuan yang belum dikembangkan"
            )
        }
    }

    val refreshAllData: suspend () -> Unit = {
        isRefreshing = true

        loadDataCounts(context)

        delay(100)

        isRefreshing = false
    }

    LaunchedEffect(Unit) {
        loadDataCounts(context)
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            coroutineScope.launch {
                refreshAllData()
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderHomeAdmin(
                navController = navController,
                adminName = name,
                adminToken = token,
                adminuserId = userId,
                adminrole = role,
                adminemail = email,
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
            //Pengajuan Section
            SectionTitle("Pengajuan")
            SubmissionSectionAdmin(
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
                                    navController.navigate("detail_usulan_admin?id=${item.id}&userId=${item.user.id}")
                                }
                                "developing" -> navController.navigate("detail_pengembangan_admin?id=${item.id}")
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
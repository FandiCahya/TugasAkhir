package com.example.applicationsop.presentation.screen.admin.list

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.applicationsop.data.DetailInfo
import androidx.compose.runtime.LaunchedEffect
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.models.Pengajuan
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.applicationsop.presentation.component.listitem.ListPengajuanItem
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.presentation.component.popup.DetailPopupUsulanAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ListPengajuanScreenAdmin1(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedDetail by remember { mutableStateOf(DetailInfo(id = "")) }
    var pengajuanList by remember { mutableStateOf<List<Pengajuan>>(emptyList()) }
    val context = LocalContext.current

        LaunchedEffect(Unit) {
            val fetchedPengajuanList = fetchPengajuanList(context,"pending")
            Log.d("PengajuanList", "Fetched Pengajuan List: $fetchedPengajuanList")
            if (fetchedPengajuanList.isEmpty()) {
                Log.d("PengajuanList", "No data available")
            }
            pengajuanList = fetchedPengajuanList // Updating the state
        }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Parsing the date format

    // Sort pengajuanList by tanggal
    val sortedPengajuanList = pengajuanList.sortedByDescending { pengajuan ->
        try {
            // Try to parse the date string to Date object
            dateFormat.parse(pengajuan.tgl) ?: Date() // Return Date() if parsing fails
        } catch (e: Exception) {
            // If parsing fails, use the current date as fallback
            Date()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HeaderWithSearch(navController = navController, title = "Pengajuan")

        Spacer(modifier = Modifier.height(20.dp))

        if (sortedPengajuanList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Add Icon with size adjustment
                    Icon(
                        imageVector = Icons.Default.Error, // Ganti dengan ikon yang diinginkan
                        contentDescription = "No Pengajuan",
                        modifier = Modifier.size(70.dp) .padding(bottom = 10.dp), // Sesuaikan ukuran ikon
                        tint = Color.Gray
                    )

                    // Add Text below the icon
                    Text(
                        text = "Tidak Ada Pengajuan",
                        color = Color.Gray,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        )
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(sortedPengajuanList) { pengajuan ->

                    ListPengajuanItem(
                        namaSistem = pengajuan.nama_sistem,
                        tanggal = pengajuan.tgl,
                        jenisSistem = pengajuan.jenis,
                        rencanaAnggaran = pengajuan.rencana_anggaran,
                        masalahSistem = pengajuan.masalah,
                        outputHasil = pengajuan.output,
                        status = pengajuan.status,
                        alasan_penolakan = pengajuan.alasan_penolakan ?: "null",
                        nama_user = pengajuan.user.name ?: "null",
                        devisi = pengajuan.user.devisi ?: "null",
                        onClick = {
                            selectedDetail = DetailInfo(
                                id = pengajuan.id,
                                namaSistem = pengajuan.nama_sistem,
                                tanggal = pengajuan.tgl.toString(),
                                jenisSistem = pengajuan.jenis,
                                rencanaAnggaran = pengajuan.rencana_anggaran,
                                masalahSistem = pengajuan.masalah,
                                outputHasil = pengajuan.output,
                                status = pengajuan.status,
                                alasan_penolakan = pengajuan.alasan_penolakan ?: "null",
                                nama_user = pengajuan.user.name ?: "null",
                                devisi = pengajuan.user.devisi ?: "null",
                            )
                            showPopup = true
                        }
                    )
                }
            }
        }
    }

    if (showPopup) {
        DetailPopupUsulanAdmin(
            onDismiss = { showPopup = false },
            id = selectedDetail.id,
            hariTanggal = selectedDetail.tanggal,
            namaSistem = selectedDetail.namaSistem,
            jenisSistem = selectedDetail.jenisSistem,
            rencanaAnggaran = selectedDetail.rencanaAnggaran,
            masalahSistem = selectedDetail.masalahSistem,
            outputHasil = selectedDetail.outputHasil,
            status = selectedDetail.status,  // Use the dynamically selected status
            alasan = if (selectedDetail.status == "Pengajuan ditolak") "Output kurang jelas" else null, // Reason only appears if the status is rejected
            namaUser = selectedDetail.nama_user,
            devisi = selectedDetail.devisi,
            isAdmin = true, // Adds the isAdmin parameter, which can be adjusted based on the user
            onAcceptClick = {
                // Action on accept (change status or perform other actions)
                refreshData(context) {updatedList -> pengajuanList = updatedList }
            },
            onRejectClick = { alasan ->
                // Action on reject with the given reason
                refreshData(context) { updatedList -> pengajuanList = updatedList }
            },
            navController = navController
        )
    }
}

private fun refreshData(context: Context ,onDataUpdated: (List<Pengajuan>) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val updatedList = fetchPengajuanList(context,"pending")
        withContext(Dispatchers.Main) {
            onDataUpdated(updatedList)
        }
    }
}





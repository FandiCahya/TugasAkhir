package com.example.applicationsop.presentation.screen.kacab

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
import com.example.applicationsop.presentation.component.listitem.ListPengajuanItem
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.presentation.component.popup.DetailPopupUsulanAdmin
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ListPengajuanKacab(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedDetail by remember { mutableStateOf(DetailInfo(id="")) }
    var pengajuanList by remember { mutableStateOf<List<Pengajuan>>(emptyList()) }

    LaunchedEffect(Unit) {
        val fetchedPengajuanList = fetchPengajuanList("pending")
        Log.d("PengajuanList", "Fetched Pengajuan List: $fetchedPengajuanList")
        if (fetchedPengajuanList.isEmpty()) {
            Log.d("PengajuanList", "No data available")
        }
        pengajuanList = fetchedPengajuanList // Updating the state
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Parsing the date format
    val todayDate = dateFormat.format(Date()) // Current date for fallback

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
                            alasan_penolakan = pengajuan.alasan_penolakan ?: "null"
                        )
                        showPopup = true
                    }
                )
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
            isAdmin = true, // Adds the isAdmin parameter, which can be adjusted based on the user
            onAcceptClick = {
                // Action on accept (change status or perform other actions)
            },
            onRejectClick = { alasan ->
                // Action on reject with the given reason
            },
            navController = navController
        )
    }
}



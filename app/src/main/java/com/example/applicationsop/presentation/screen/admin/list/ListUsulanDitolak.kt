package com.example.applicationsop.presentation.screen.admin.list

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
import java.util.Date
import java.util.Locale


@Composable
fun ListPengajuanScreenAdmin2(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedDetail by remember { mutableStateOf(DetailInfo(id="")) }
    var pengajuanList by remember { mutableStateOf<List<Pengajuan>>(emptyList()) }


    LaunchedEffect(Unit) {
        // Fetching the data when the Composable is first launched
        val fetchedPengajuanList = fetchPengajuanList("rejected") // Fetch the data
        pengajuanList = fetchedPengajuanList // Updating the state
//        println("Pengajuan List View :${pengajuanList}")
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
        // Header with back button and search icon
        HeaderWithSearch(navController = navController, title = "Pengajuan")
        Spacer(modifier = Modifier.height(20.dp))
        var currentDate: String? = null
        // List of submissions
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sortedPengajuanList) { pengajuan ->
                var formattedDate: String
                try {
                    // Try to parse the date string and format it
                    val parsedDate = dateFormat.parse(pengajuan.tgl)
                    formattedDate = dateFormat.format(parsedDate ?: Date()) // If parsing fails, fallback to current date
                } catch (e: Exception) {
                    // If parsing fails, fallback to current date
                    formattedDate = todayDate
                }

                // Using data from the API response dynamically
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
            status = selectedDetail.status,  // Gunakan status yang dipilih secara dinamis
            alasan = if (selectedDetail.status == "Pengajuan ditolak") "Output kurang jelas" else null, // Alasan hanya muncul jika status ditolak
            isAdmin = true, // Menambahkan parameter isAdmin yang bisa ditentukan sesuai pengguna
            onAcceptClick = {
                // Aksi terima (ubah status atau lakukan tindakan lainnya)
            },
            onRejectClick = { alasan ->
                // Aksi tolak dengan alasan yang dimasukkan
            },
            navController = navController
        )
    }
}
package com.example.applicationsop.presentation.screen.admin.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.data.DetailInfo
import com.example.applicationsop.presentation.component.HeaderWithSearch
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import androidx.compose.runtime.LaunchedEffect
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.models.Pengajuan
import androidx.compose.foundation.lazy.items
import com.example.applicationsop.data.ListPengajuanItem
import com.example.applicationsop.presentation.component.DetailPopupUsulanAdmin
import java.text.SimpleDateFormat
import java.util.Date

fun formatTanggal2(tanggal: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
    val outputFormat = SimpleDateFormat("yyyy-MM-dd") // Format yang hanya menampilkan tanggal
    val date: Date = inputFormat.parse(tanggal)
    return outputFormat.format(date) // Mengembalikan tanggal yang diformat
}

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with back button and search icon
        HeaderWithSearch(navController = navController, title = "Pengajuan")
        Spacer(modifier = Modifier.height(20.dp))

        // List of submissions
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(pengajuanList) { pengajuan ->
                // Using data from the API response dynamically
                ListPengajuanItem(
                    namaSistem = pengajuan.nama_sistem,
                    tanggal = pengajuan.tgl,
                    jenisSistem = pengajuan.jenis,
                    rencanaAnggaran = pengajuan.rencana_anggaran,
                    masalahSistem = pengajuan.masalah,
                    outputHasil = pengajuan.output,
                    status = pengajuan.status,
                    onClick = {
                        selectedDetail = DetailInfo(
                            id = pengajuan.id,
                            namaSistem = pengajuan.nama_sistem,
                            tanggal = pengajuan.tgl.toString(),
                            jenisSistem = pengajuan.jenis,
                            rencanaAnggaran = pengajuan.rencana_anggaran,
                            masalahSistem = pengajuan.masalah,
                            outputHasil = pengajuan.output,
                            status = pengajuan.status
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
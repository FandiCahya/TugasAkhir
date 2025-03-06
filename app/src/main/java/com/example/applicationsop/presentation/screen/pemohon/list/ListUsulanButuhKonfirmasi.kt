package com.example.applicationsop.presentation.screen.pemohon.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.data.DetailInfo
import com.example.applicationsop.data.ListPengajuanItem
import com.example.applicationsop.models.Pengajuan
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.presentation.component.popup.DetailPopupUsulanUser
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ListPengajuanScreenButuhKonfirmasi(
    navController: NavController,
    role: String?,
    devisi: String?
) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedDetail by remember { mutableStateOf(DetailInfo(id = "")) }
    var pengajuanList by remember { mutableStateOf<List<Pengajuan>>(emptyList()) }

    @Composable
    fun refreshList() {
        LaunchedEffect(role, devisi) {
            if (role != null && devisi != null) {
                val fetchedPengajuanList = fetchPengajuanList("pending", role, devisi)
//            println("Role: $role, Devisi: $devisi")
                pengajuanList = fetchedPengajuanList // Updating the state with fetched data
            }
        }
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

    refreshList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        HeaderWithSearch(navController = navController, title = "Pengajuan")

        Spacer(modifier = Modifier.height(20.dp))

        var currentDate: String? = null

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

                // Only display a header for a new date
                if (currentDate != formattedDate) {
                    currentDate = formattedDate

                    // Create a row with dividers and the date text in the middle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        // Left divider
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        )

                        // Text in the middle
                        Text(
                            text = "$formattedDate",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(horizontal = 8.dp) // Padding kiri dan kanan pada teks
                        )

                        // Right divider
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
                ListPengajuanItem(
                    namaSistem = pengajuan.nama_sistem,
                    tanggal = pengajuan.tgl,
                    jenisSistem = pengajuan.jenis,
                    rencanaAnggaran = pengajuan.rencana_anggaran,
                    masalahSistem = pengajuan.masalah,
                    outputHasil = pengajuan.output,
                    status = pengajuan.status,
                    alasan_penolakan = pengajuan.alasan_penolakan
                        ?: "null",  // Use the dynamically selected alasan
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
        DetailPopupUsulanUser(
            onDismiss = { showPopup = false },
            id = selectedDetail.id,  // Use the dynamically selected id
            hariTanggal = selectedDetail.tanggal,
            namaSistem = selectedDetail.namaSistem,
            jenisSistem = selectedDetail.jenisSistem,
            rencanaAnggaran = selectedDetail.rencanaAnggaran,
            masalahSistem = selectedDetail.masalahSistem,
            outputHasil = selectedDetail.outputHasil,
            status = selectedDetail.status,  // Use the dynamically selected status
            alasan = selectedDetail.alasan_penolakan, // Alasan hanya muncul jika status ditolak
            onEditClick = {
                navController.navigate("form_usulan") // Ganti dengan rute yang sesuai
            }
        )
    }
}




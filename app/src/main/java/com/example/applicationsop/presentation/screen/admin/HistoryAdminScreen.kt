package com.example.applicationsop.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchLaporanList
import com.example.applicationsop.data.DetailPengujian
import com.example.applicationsop.models.Laporan
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.presentation.component.listitem.ListLaporanItem
import com.example.applicationsop.presentation.component.popup.RiwayatPopup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log
import com.example.applicationsop.data.DetailLaporan


@Composable
fun HistoryAdmin(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedDetail by remember { mutableStateOf(DetailLaporan("")) }
    var laporanList by remember { mutableStateOf<List<Laporan>>(emptyList()) }


    LaunchedEffect(Unit) {
        // Fetching the data when the Composable is first launched
        val fetchLaporanList = fetchLaporanList(status_pengajuan="finished") // Fetch the data
        Log.d("FETCH_LAPORAN", fetchLaporanList.toString())
        laporanList = fetchLaporanList // Updating the state
    }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Parsing the date format
    val todayDate = dateFormat.format(Date()) // Current date for fallback
    // Sort pengajuanList by tanggal
    val sortedLaporanList = laporanList.sortedByDescending { laporan ->
        try {
            dateFormat.parse(laporan.pengajuan.tgl) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HeaderWithSearch(navController = navController, title = "Riwayat")
        Spacer(modifier = Modifier.height(20.dp))
        var currentDate: String? = null

        // List of pengujian items from fetched data
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sortedLaporanList) { laporan ->
                val pengajuan = laporan.pengajuan
                val pengembangan = laporan.pengembangan
                val pengujian = laporan.pengujian
                val persetujuan = laporan.persetujuan_pengujian
                val detailPersetujuan = laporan.persetujuan_pengujian_details
                ListLaporanItem(
                    tgl = pengajuan.tgl,
                    nama_sistem = pengajuan.nama_sistem,
                    jenis = pengajuan.jenis,
                    rencana_anggaran = pengajuan.rencana_anggaran,
                    masalah = pengajuan.masalah,
                    output = pengajuan.output ,
                    tanggal_mulai = pengembangan?.tanggal_mulai,
                    tanggal_selesai = pengembangan?.tanggal_selesai,
                    tahap = pengembangan?.tahap,
                    keterangan = pengembangan?.keterangan,
                    perangkat_lunak = pengujian?.perangkat_lunak,
                    versiPerangkat = pengujian?.versi,
                    tujuanPengujian = pengujian?.tujuan,
                    metodePengujian = pengujian?.metode,
                    detailPersetujuan = laporan.persetujuan_pengujian_details,
                    status = pengajuan.status,
                    onClick = {
                        selectedDetail = DetailLaporan(
                            id = laporan.pengajuan.id,
                            tgl = laporan.pengajuan.tgl,
                            nama_sistem = laporan.pengajuan.nama_sistem,
                            jenis = laporan.pengajuan.jenis,
                            rencana_anggaran = laporan.pengajuan.rencana_anggaran,
                            masalah = laporan.pengajuan.masalah,
                            output = laporan.pengajuan.output,
                            tanggal_mulai = laporan.pengembangan?.tanggal_mulai,
                            tanggal_selesai = laporan.pengembangan?.tanggal_selesai,
                            tahap = laporan.pengembangan?.tahap,
                            keterangan = laporan.pengembangan?.keterangan,
                            perangkat_lunak = laporan.pengujian?.perangkat_lunak,
                            versiPerangkat = laporan.pengujian?.versi,
                            tujuanPengujian = laporan.pengujian?.tujuan,
                            metodePengujian = laporan.pengujian?.metode,
                            detailPersetujuan = laporan.persetujuan_pengujian_details,
                            status = laporan.pengajuan.status
                        )
                        showPopup = true
                    }
                )
            }
        }
    }

    if (showPopup && selectedDetail != null) {
        RiwayatPopup(
            onDismiss = { showPopup = false },
            navController = navController,
            id = selectedDetail.id,
            tgl = selectedDetail.tgl,
            namaSistem = selectedDetail.nama_sistem,
            jenis = selectedDetail.jenis,
            rencanaAnggaran = selectedDetail.rencana_anggaran,
            masalah = selectedDetail.masalah,
            output = selectedDetail.output,
            tanggalMulai = selectedDetail.tanggal_mulai,
            tanggalSelesai = selectedDetail.tanggal_selesai,
            tahap = selectedDetail.tahap,
            keterangan = selectedDetail.keterangan,
            perangkatLunak = selectedDetail.perangkat_lunak,
            versiPerangkat = selectedDetail.versiPerangkat,
            tujuanPengujian = selectedDetail.tujuanPengujian,
            metodePengujian = selectedDetail.metodePengujian,
            detailPersetujuan = selectedDetail.detailPersetujuan,
            status = selectedDetail.status
        )
    }
}
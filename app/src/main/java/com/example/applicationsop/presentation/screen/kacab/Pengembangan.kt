package com.example.applicationsop.presentation.screen.kacab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengembanganList
import com.example.applicationsop.models.Pengembangan
import com.example.applicationsop.presentation.screen.pemohon.ScheduleItem
import androidx.compose.foundation.lazy.items
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.presentation.component.listitem.ListPengembangan
import com.example.applicationsop.presentation.component.popup.SchedulePopupUser
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ListPengembanganKacab(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedScheduleItem by remember { mutableStateOf<ScheduleItem?>(null) }
    var pengembanganList by remember { mutableStateOf<List<Pengembangan>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Fetching the data when the Composable is first launched
        val fetchedPengembanganList = fetchPengembanganList() // Fetch the data
        pengembanganList = fetchedPengembanganList // Updating the state
        println("Pengembangan List View :${pengembanganList}")
    }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Parsing the date format
    val todayDate = dateFormat.format(Date()) // Current date for fallback
    // Sort pengajuanList by tanggal
    val sortedPengembanganList = pengembanganList.sortedByDescending { pengembangan ->
        try {
            // Try to parse the date string to Date object
            dateFormat.parse(pengembangan.tanggal_mulai) ?: Date() // Return Date() if parsing fails
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
        HeaderWithSearch(navController = navController, title = "Pengembangan")
        Spacer(modifier = Modifier.height(20.dp))

        // List of submissions
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Use `items` to iterate over the list of `pengembanganList`
            items(sortedPengembanganList) { pengembangan ->

                // Create a ScheduleItem from Pengembangan data
                val scheduleItem = ScheduleItem(
                    task = pengembangan.pengajuan.nama_sistem, // Nama sistem from Pengajuan
                    id = pengembangan.id, // ID from Pengembangan
                    startDate = pengembangan.tanggal_mulai, // Start date
                    endDate = pengembangan.tanggal_selesai, // End date
                    description = pengembangan.keterangan, // Description from Pengembangan
                    stage = pengembangan.tahap, // Stage from Pengembangan
                    progressPercentage = pengembangan.persentase, // Progress from Pengembangan
                    status = if (pengembangan.persentase == 100) "testing" else "developed" // Logic for status based on progress
                )

                // Pass actual schedule data to the ListPengembangan composable
                ListPengembangan(
                    namaSistem = pengembangan.pengajuan.nama_sistem,
                    status = pengembangan.status,
                    startDate = pengembangan.tanggal_mulai,
                    scheduleItem = scheduleItem,
                    onClick = { clickedSchedule ->
                        selectedScheduleItem = clickedSchedule // Set the selected schedule
                        showPopup = true // Show the popup
                    }
                )
            }
        }
    }

    // Show popup only when it's true
    selectedScheduleItem?.let { scheduleItem ->
        if (showPopup) {
            SchedulePopupUser(
                onDismiss = { showPopup = false },
                scheduleItem = scheduleItem
            )
        }
    }
}
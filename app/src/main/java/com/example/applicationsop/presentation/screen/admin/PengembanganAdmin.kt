package com.example.applicationsop.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengembanganList
import com.example.applicationsop.models.Pengembangan
import com.example.applicationsop.presentation.screen.pemohon.ScheduleItem
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import androidx.compose.foundation.lazy.items
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.presentation.component.popup.SchedulePopup

@Composable
fun ListPengembangan(
    namaSistem: String,
    status: String,
    scheduleItem: ScheduleItem, // Add scheduleItem to pass the details
    onClick: (ScheduleItem) -> Unit // Function to handle the click
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .background(
                Color(0xFFF6F6F6),
                RoundedCornerShape(20.dp)
            ) // Set the background to light gray
            .padding(16.dp)
            .clickable { onClick(scheduleItem) }, // Handle click
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular icon (human icon) with border
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    kuning,
                    shape = CircleShape
                ) // Yellow background for the circle
                .border(2.dp, Color.Black, shape = CircleShape) // Border around the circle
        ) {
            // Replace image with Android's default Person Icon
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile Icon",
                modifier = Modifier.fillMaxSize(),
                tint = Color.Black // Adjust the color if needed
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f) // Allow column to take remaining space
        ) {
            Text(
                text = namaSistem,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp)) // Small space between the texts

            Divider(
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            val statusText = when (status) {
                "developed" -> "Pengembangan"
                "finished" -> "Pengembangan Selesai"
                else -> "Status Tidak Dikenali" // Default for other statuses
            }

            Text(
                text = statusText,
                fontSize = 14.sp,
                color = when (status) {
                    "developed" -> kuning
                    "finished" -> ijo
                    else -> Color.Black
                }
            )
        }
    }
}


@Composable
fun ListPengembanganAdminScreen(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedScheduleItem by remember { mutableStateOf<ScheduleItem?>(null) }
    var pengembanganList by remember { mutableStateOf<List<Pengembangan>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Fetching the data when the Composable is first launched
        val fetchedPengembanganList = fetchPengembanganList() // Fetch the data
        pengembanganList = fetchedPengembanganList // Updating the state
        println("Pengembangan List View :${pengembanganList}")
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
            items(pengembanganList) { pengembangan ->
                // Create a ScheduleItem from Pengembangan data
                val scheduleItem = ScheduleItem(
                    task = pengembangan.pengajuan.nama_sistem, // Nama sistem from Pengajuan
                    startDate = pengembangan.tanggal_mulai, // Start date
                    endDate = pengembangan.tanggal_selesai, // End date
                    description = pengembangan.keterangan, // Description from Pengembangan
                    stage = pengembangan.tahap, // Stage from Pengembangan
                    progressPercentage = pengembangan.persentase, // Progress from Pengembangan
                    status = if (pengembangan.persentase == 100) "finished" else "developed" // Logic for status based on progress
                )

                // Pass actual schedule data to the ListPengembangan composable
                ListPengembangan(
                    namaSistem = scheduleItem.task,
                    status = pengembangan.status, // Status from Pengembangan
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
            SchedulePopup(
                onDismiss = { showPopup = false },
                onSave = { newSchedule ->
                    // Fungsi untuk menyimpan jadwal baru
                    // Update schedule item yang dipilih dengan data baru
                    selectedScheduleItem = newSchedule
                    showPopup = false // Menutup popup setelah menyimpan
                },
                taskName = scheduleItem.task,
                startDate = scheduleItem.startDate,
                endDate = scheduleItem.endDate,
                description = scheduleItem.description,
                selectedStages = scheduleItem.stage.split(", "), // Misalnya, split tahapan yang dipilih
                progressPercentage = scheduleItem.progressPercentage,
                status = scheduleItem.status, // Passing the status value
                navController = navController // Passing navController
            )
        }
    }
}


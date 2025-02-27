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
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengembanganList
import com.example.applicationsop.models.Pengembangan
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.presentation.component.HeaderWithSearch
import com.example.applicationsop.presentation.screen.pemohon.ScheduleItem
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import androidx.compose.foundation.lazy.items

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

            Text(
                text = status,
                fontSize = 14.sp,
                color = when (status) {
                    "developed" -> kuning // Ganti dengan warna Maroon dari tema Anda
                    "Pengujian ditolak" -> abang // Ganti dengan warna abang
                    "Pengajuan Diterima" -> ijo // Ganti dengan warna hijau dari tema Anda
                    "finished" -> ijo
                    else -> Color.Black // Default jika status tidak dikenali
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
                    progressPercentage = pengembangan.persentase // Progress from Pengembangan
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
                progressPercentage = scheduleItem.progressPercentage
            )
        }
    }
}

@Composable
fun SchedulePopup(
    onDismiss: () -> Unit,
    onSave: (ScheduleItem) -> Unit,
    taskName: String,
    startDate: String,
    endDate: String,
    description: String,
    selectedStages: List<String>,
    progressPercentage: Int
) {
    var taskNameState by remember { mutableStateOf(taskName) }
    var startDateState by remember { mutableStateOf(startDate) }
    var endDateState by remember { mutableStateOf(endDate) }
    var descriptionState by remember { mutableStateOf(description) }
    var selectedStagesState by remember { mutableStateOf(selectedStages) }
    var progressPercentageState by remember { mutableStateOf(progressPercentage) }

    val isDropdownExpanded = remember { mutableStateOf(false) }

    // Menyimpan status tahap pengerjaan
    var completedStagesState by remember { mutableStateOf(selectedStagesState) }
    var ongoingStageState by remember { mutableStateOf("") } // Tahap yang sedang dikerjakan

    val availableStages = listOf("Analisis", "Desain UI/UX", "Pengerjaan", "Penyelesaian", "Testing")

    Box(modifier = Modifier.fillMaxSize()) {
        // Gelap di latar belakang
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)) // Gelapkan background
                .clickable { onDismiss() } // Menutup popup jika area gelap di klik
        )

        // Card Popup
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(20.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header dengan Icon dan Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = "Timer Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Maroon
                    )
                }

                Text(
                    text = "Jadwal Pengembangan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 40.dp)
                )

                // Line separator
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Konten dengan dua kolom (label dan nilai)
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Perangkat yang dikembangkan
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Perangkat", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(": $taskNameState", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Tanggal Mulai
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tanggal Mulai", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(": $startDateState", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Tanggal Selesai
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tanggal Selesai", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(": $endDateState", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Keterangan
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Keterangan", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(": $descriptionState", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Kolom untuk label "Progres"
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Progres", fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        // Kolom untuk Circular Progress Indicator dengan persentase
                        Column(
                            modifier = Modifier
                                .weight(2f),
                            horizontalAlignment = Alignment.CenterHorizontally // Memastikan isinya di tengah
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(100.dp) // Ukuran untuk CircularProgressIndicator
                            ) {
                                CircularProgressIndicator(
                                    progress = progressPercentageState / 100f, // Menghitung progres
                                    modifier = Modifier.fillMaxSize(),
                                    color = Maroon, // Warna progres
                                    strokeWidth = 20.dp // Lebar lingkaran
                                )

                                // Persentase di tengah Circular Progress Indicator
                                Text(
                                    text = "$progressPercentageState%",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Update dan Close berdampingan
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End // Memberi jarak antar tombol
                    ) {
                        // Tombol Update
                        Button(
                            onClick = {
                                val schedule = ScheduleItem(
                                    task = taskNameState,
                                    startDate = startDateState,
                                    endDate = endDateState,
                                    description = descriptionState,
                                    stage = completedStagesState.joinToString(", "),
                                    progressPercentage = progressPercentageState
                                )
                                onSave(schedule) // Fungsi untuk menyimpan atau memperbarui jadwal
                            },
                            modifier = Modifier
                                .width(120.dp)
                                .padding(end = 10.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Update", color = Color.White) // Mengubah teks tombol menjadi "Update"
                        }

                        // Tombol Close
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .width(100.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Tutup", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderComposablePengembangan(title: String, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start // Align items to start
    ) {
        // Tombol Kembali (Back)
        BackButton(navController = navController, colorVersion = "m")

        // Space between back button and title
        Spacer(modifier = Modifier.width(16.dp))

        // Title
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Start
        )
    }
}


package com.example.applicationsop.presentation.screen.pemohon

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
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning

// Data model for the schedule
data class ScheduleItem(
    val task: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val stage: String,
    val progressPercentage: Int,
    val status: String
)

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
                    "Menunggu konfirmasi" -> kuning // Ganti dengan warna Maroon dari tema Anda
                    "Pengujian ditolak" -> abang // Ganti dengan warna abang
                    "Pengajuan Diterima" -> ijo // Ganti dengan warna hijau dari tema Anda
                    "Pengembangan" -> ijo
                    else -> Color.Black // Default jika status tidak dikenali
                }
            )
        }
    }
}


@Composable
fun ListPengembanganScreen(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedScheduleItem by remember { mutableStateOf<ScheduleItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with back button and search icon
        HeaderWithSearch(navController = navController, title = "Pengembangan" )

        Spacer(modifier = Modifier.height(20.dp))

        // List of submissions
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(2) { index ->
                // Sample schedule data
                val scheduleItem = ScheduleItem(
                    task = "Task 1",
                    startDate = "2025-03-01",
                    endDate = "2025-03-10",
                    description = "Task Description",
                    stage = "Desain UI/UX",
                    progressPercentage = 50,
                    status = "developed" // Set status here
                )


                // Pass actual schedule data to the ListPengembangan composable
                ListPengembangan(
                    namaSistem = scheduleItem.task,
                    status = "Pengembangan",
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
                scheduleItem = scheduleItem
            )
        }
    }
}

@Composable
fun SchedulePopup(
    onDismiss: () -> Unit,
    scheduleItem: ScheduleItem, // Pass the selected schedule item to display details
) {
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
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Icon and Title
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
                    text = "Jadwal Progres Pengembangan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 40.dp)
                )

                // Line separator
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Content with updated data
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Row for Nama Sistem
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Kolom 1 (Judul)
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Perangkat yang dikembangkan", fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        // Kolom 2 (Isi)
                        Column(
                            modifier = Modifier.weight(2f)
                        ) {
                            Text(": ${scheduleItem.task}", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row for Tanggal Mulai
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Kolom 1 (Judul)
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Tanggal Mulai", fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        // Kolom 2 (Isi)
                        Column(
                            modifier = Modifier.weight(2f)
                        ) {
                            Text(": ${scheduleItem.startDate}", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row for Tanggal Selesai
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Kolom 1 (Judul)
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "Tanggal Selesai",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        // Kolom 2 (Isi)
                        Column(
                            modifier = Modifier.weight(2f)
                        ) {
                            Text(": ${scheduleItem.endDate}", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row for Keterangan (Deskripsi)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Kolom 1 (Judul)
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Keterangan", fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        // Kolom 2 (Isi)
                        Column(
                            modifier = Modifier.weight(2f)
                        ) {
                            Text(": ${scheduleItem.description}", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row for Tahap (Stage)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Kolom 1 (Judul)
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Tahap", fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        // Kolom 2 (Isi)
                        Column(
                            modifier = Modifier.weight(2f)
                        ) {
                            Text(": ${scheduleItem.stage}", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row for Progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Kolom 1 (Judul)
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Progres", fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        // Kolom 2 (Isi)
                        Column(
                            modifier = Modifier.weight(2f)
                        ) {
                            Text(": ${scheduleItem.progressPercentage}", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row for Persentase (Progress Bar)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Kolom 1 (Judul)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Persentase",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            // Kolom 2 (Isi)
                            Column(modifier = Modifier.weight(2f)) {
                                // Circular Progress Indicator
                                CircularProgressIndicator(
                                    progress = scheduleItem.progressPercentage.toFloat() / 100f,
                                    modifier = Modifier
                                        .size(50.dp) // Adjust size
                                        .align(Alignment.CenterHorizontally),
                                    color = Maroon,
                                    strokeWidth = 8.dp // Adjust stroke width
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(": ${scheduleItem.progressPercentage}%", color = Color.Black)
                            }
                        }
                    }


                    // Close Button
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(), // Take full width
                        horizontalArrangement = Arrangement.End // Align to the right
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .padding(end = 16.dp) // Optional padding to give some space from the edge
                                .width(100.dp)
                                .shadow(
                                    4.dp,
                                    RoundedCornerShape(16.dp)
                                ), // Set the width of the button to a smaller size
                            colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Tutup",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
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
}


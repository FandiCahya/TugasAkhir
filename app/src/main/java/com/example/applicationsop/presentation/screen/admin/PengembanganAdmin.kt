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
    val progressPercentage: Int // Ensure this is an Int
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
fun ListPengembanganAdminScreen(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedScheduleItem by remember { mutableStateOf<ScheduleItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with back button and search icon
        Rectangle1217(navController = navController)
        Spacer(modifier = Modifier.height(20.dp))

        // List of submissions
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(2) { index ->
                // Sample schedule data
                val scheduleItem = ScheduleItem(
                    task = "Nama Sistem ${index + 1}",
                    startDate = "25/10/2025",
                    endDate = "30/10/2025",
                    description = "Deskripsi singkat",
                    stage = "Desain Ui/Ux",
                    progressPercentage = 50
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
                onSave = { newSchedule ->
                    // Fungsi untuk menyimpan jadwal baru
                    // Anda bisa menambahkan jadwal baru ke dalam list atau database sesuai kebutuhan
                    showPopup = false // Menutup popup setelah menyimpan
                }
            )
        }
    }
}

@Composable
fun SchedulePopup(
    onDismiss: () -> Unit,
    onSave: (ScheduleItem) -> Unit // Fungsi untuk menyimpan jadwal
) {
    var taskName by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedStages by remember { mutableStateOf(listOf<String>()) } // Untuk menyimpan tahap yang dipilih
    var progressPercentage by remember { mutableStateOf(0) }

    val availableStages =
        listOf("Analisis", "Desain UI/UX", "Pengerjaan", "Penyelesaian", "Testing")

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
                // Title
                Text(
                    text = "Tambah Jadwal Pengembangan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 40.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input for Nama Perangkat
                TextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Perangkat yang dikembangkan") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input for Tanggal Mulai
                TextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Tanggal Mulai") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input for Tanggal Selesai
                TextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("Tanggal Selesai") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input for Keterangan
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Keterangan") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // RadioButton untuk memilih tahapan
                Text("Pilih Tahap Pengerjaan:", fontWeight = FontWeight.Bold)
                availableStages.forEach { stage ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        // RadioButton untuk tiap tahap
                        RadioButton(
                            selected = selectedStages.contains(stage),
                            onClick = {
                                if (selectedStages.contains(stage)) {
                                    selectedStages = selectedStages.filter { it != stage }
                                } else {
                                    selectedStages = selectedStages + stage
                                }

                                // Menghitung persentase berdasarkan tahapan yang dipilih
                                progressPercentage =
                                    (selectedStages.size * 100) / availableStages.size
                            }
                        )
                        Text(stage, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Menampilkan progress dan persentase
                Text("Progres: $progressPercentage%", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                // Button Save
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            // Menyimpan data jadwal progres
                            val schedule = ScheduleItem(
                                task = taskName,
                                startDate = startDate,
                                endDate = endDate,
                                description = description,
                                stage = selectedStages.joinToString(", "),
                                progressPercentage = progressPercentage
                            )
                            onSave(schedule) // Fungsi untuk menyimpan jadwal
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Simpan", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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


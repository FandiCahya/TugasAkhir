package com.example.applicationsop.presentation.component.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.applicationsop.Api.updatePengembangan
import com.example.applicationsop.models.UpdatePengembangan
import com.example.applicationsop.presentation.screen.pemohon.ScheduleItem
import com.example.applicationsop.ui.theme.Maroon
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SchedulePopupAdmin(
    onDismiss: () -> Unit,
    onSave: (ScheduleItem) -> Unit,
    taskName: String,
    id: String,
    startDate: String,
    endDate: String,
    description: String,
    selectedStages: List<String>,
    progressPercentage: Int,
    status: String, // Adding status to the parameters
    navController: NavController // Adding navController for navigation to the new form
) {

    var taskNameState by remember { mutableStateOf(taskName) }
    var idState by remember { mutableStateOf(id) }
    var startDateState by remember { mutableStateOf(startDate) }
    var endDateState by remember { mutableStateOf(endDate) }
    var descriptionState by remember { mutableStateOf(description) }
    var selectedStagesState by remember { mutableStateOf(selectedStages) }
    var progressPercentageState by remember { mutableStateOf(progressPercentage) }

    val availableStages = listOf("Analisis", "Desain UI/UX", "Pengerjaan", "Penyelesaian", "Testing")
    val coroutineScope = rememberCoroutineScope()
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
                    modifier = Modifier.padding(start = 80.dp)
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

                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.Gray, // Set the color of the divider
                        thickness = 1.dp // Set the thickness of the divider
                    )

                    // Tahap Pengerjaan
                    Text(
                        if (status == "finished") "Tahap Pengerjaan" else "Pilih Tahap Pengerjaan",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                    // Checkbox tahapan pengerjaan
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Left Column
                        Column(modifier = Modifier.weight(1f)) {
                            availableStages.take(3).forEach { stage ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = selectedStagesState.contains(stage),
                                        onCheckedChange = { isChecked ->
                                            selectedStagesState = if (isChecked) {
                                                selectedStagesState + stage
                                            } else {
                                                selectedStagesState - stage
                                            }
                                            // Update progress based on selected stages
                                            progressPercentageState = (selectedStagesState.size * 100) / availableStages.size
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Maroon,
                                            uncheckedColor = Color.LightGray,
                                            checkmarkColor = Color.White,
                                            disabledCheckedColor = Maroon,
                                            disabledUncheckedColor = Color.LightGray
                                        ),
                                        enabled = status != "finished" // Disable the checkbox when status is "finished"
                                    )
                                    Text(stage, modifier = Modifier.padding(start = 8.dp), color = Maroon)
                                }
                            }
                        }

                        // Right Column
                        Column(modifier = Modifier.weight(1f)) {
                            availableStages.drop(3).forEach { stage ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = selectedStagesState.contains(stage),
                                        onCheckedChange = { isChecked ->
                                            selectedStagesState = if (isChecked) {
                                                selectedStagesState + stage
                                            } else {
                                                selectedStagesState - stage
                                            }
                                            // Update progress based on selected stages
                                            progressPercentageState = (selectedStagesState.size * 100) / availableStages.size
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Maroon,
                                            uncheckedColor = Color.LightGray,
                                            checkmarkColor = Color.White,
                                            disabledCheckedColor = Maroon,
                                            disabledUncheckedColor = Color.LightGray
                                        ),
                                        enabled = status != "finished" // Disable the checkbox when status is "finished"
                                    )
                                    Text(stage, modifier = Modifier.padding(start = 8.dp), color = Maroon)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Update progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Progres: $progressPercentageState%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Maroon
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Update and Form Pengujian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (status == "developed") {
                            Button(
                                onClick = {
                                    coroutineScope.launch { // Run inside coroutine
                                        val updatedPengembangan = UpdatePengembangan(
                                            tahap = selectedStagesState.joinToString(", "),
                                            persentase = progressPercentageState,
                                            status = if (progressPercentageState == 100) "finished" else "developed"
                                        )
                                        updatePengembangan(idState, updatedPengembangan) // Call the update API
                                        onDismiss() // Close the popup
                                    }
                                },
                                modifier = Modifier
                                    .width(180.dp)
                                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                                colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Update", color = Color.White)
                            }
                        } else if (status == "finished") {
                            // Tombol Form Pengujian di kiri
                            Button(
                                onClick = {
                                    navController.navigate("formPengujian?id=$id&taskName=$taskName") // Navigate to FormPengujianAdmin screen
                                },
                                modifier = Modifier
                                    .width(180.dp)
                                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                                colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Form Pengujian", color = Color.White)
                            }
                        }

                        // Tombol Close di kanan
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
fun SchedulePopupUser(
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
                            Text(
                                "Nama Perangkat",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
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
                            Text(": ${scheduleItem.progressPercentage}%", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp), // Padding untuk row kedua
                        horizontalArrangement = Arrangement.Center // Menempatkan Circular Progress Bar di tengah
                    ) {
                        // Circular Progress Bar berada di tengah bawah
                        CircularProgressIndicator(
                            progress = scheduleItem.progressPercentage.toFloat() / 100f, // Menyesuaikan nilai progress
                            modifier = Modifier.size(50.dp), // Ukuran progress bar
                            color = Maroon, // Warna progress bar
                            strokeWidth = 8.dp // Ketebalan garis progress bar
                        )
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
}
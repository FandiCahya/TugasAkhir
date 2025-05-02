package com.example.applicationsop.presentation.screen.pemohon

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengembanganSortList
import com.example.applicationsop.models.Pengembangan
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.presentation.component.listitem.ListPengembangan
import com.example.applicationsop.presentation.component.popup.SchedulePopupUser
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data model for the schedule
data class ScheduleItem(
    val task: String,
    val id: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val stage: String,
    val progressPercentage: Int,
    val status: String
)

fun getUserData(context: Context): Map<String, String?> {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
    val token = sharedPreferences.getString("TOKEN", null)
    val userId = sharedPreferences.getString("USER_ID", null)
    val role = sharedPreferences.getString("ROLE", null)
    val name = sharedPreferences.getString("NAME", null)
    val email = sharedPreferences.getString("EMAIL", null)
    val devisi = sharedPreferences.getString("DEVISI", null)

    return mapOf(
        "token" to token,
        "userId" to userId,
        "role" to role,
        "name" to name,
        "email" to email,
        "devisi" to devisi
    )
}

@Composable
fun ListPengembanganScreen(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedScheduleItem by remember { mutableStateOf<ScheduleItem?>(null) }
    var pengembanganList by remember { mutableStateOf<List<Pengembangan>>(emptyList()) }

    // Mengambil data dari SharedPreferences
    val context = LocalContext.current
    val userData = getUserData(context)

    // Menyimpan role, devisi, dan userId ke dalam variabel
    val role = userData["role"]
    val devisi = userData["devisi"]
    val userId = userData["userId"]

    LaunchedEffect(Unit) {

        if (role != null && devisi != null) {
            val fetchedPengembanganList =
                fetchPengembanganSortList(role, devisi, userId) // Fetch the data
            pengembanganList = fetchedPengembanganList // Updating the state
            println("Pengembangan List View :${pengembanganList}")
        }

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

        if (sortedPengembanganList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Add Icon with size adjustment
                    Icon(
                        imageVector = Icons.Default.Error, // Ganti dengan ikon yang diinginkan
                        contentDescription = "No Pengajuan",
                        modifier = Modifier.size(70.dp) .padding(bottom = 10.dp), // Sesuaikan ukuran ikon
                        tint = Color.Gray
                    )

                    // Add Text below the icon
                    Text(
                        text = "Tidak Ada Pengembangan",
                        color = Color.Gray,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        )
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(sortedPengembanganList) { pengembangan ->
                    val scheduleItem = ScheduleItem(
                        task = pengembangan.pengajuan.nama_sistem, // Nama sistem from Pengajuan
                        id = pengembangan.id, // ID from Pengembangan
                        startDate = pengembangan.tanggal_mulai, // Start date
                        endDate = pengembangan.tanggal_selesai, // End date
                        description = pengembangan.keterangan, // Description from Pengembangan
                        stage = pengembangan.tahap, // Stage from Pengembangan
                        progressPercentage = pengembangan.persentase, // Progress from Pengembangan
                        status = if (pengembangan.persentase == 100) "finished" else "developed" // Logic for status based on progress
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


package com.example.applicationsop.presentation.screen.pemohon

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.Api.fetchPengembanganList
import com.example.applicationsop.Api.fetchPengembanganSortList
import com.example.applicationsop.models.Pengembangan
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.presentation.component.popup.SchedulePopupUser
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
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
                "developed" -> "Sedang Dikembangkan"
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
            val fetchedPengembanganList = fetchPengembanganSortList(role,devisi,userId) // Fetch the data
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
        HeaderWithSearch(navController = navController, title = "Pengembangan" )

        Spacer(modifier = Modifier.height(20.dp))

        var currentDate: String? = null

        // List of submissions
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sortedPengembanganList) { pengembangan ->
                var formattedDate: String
                try {
                    // Try to parse the date string and format it
                    val parsedDate = dateFormat.parse(pengembangan.tanggal_mulai)
                    formattedDate = dateFormat.format(parsedDate ?: Date()) // If parsing fails, fallback to current date
                } catch (e: Exception) {
                    // If parsing fails, fallback to current date
                    formattedDate = todayDate
                }

                // Create a ScheduleItem from Pengembangan data
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
            SchedulePopupUser(
                onDismiss = { showPopup = false },
                scheduleItem = scheduleItem
            )
        }
    }
}


package com.example.applicationsop.presentation.component.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.presentation.screen.pemohon.ScheduleItem
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ListPengembangan(
    namaSistem: String,
    status: String,
    startDate: String, // Menambahkan startDate
    scheduleItem: ScheduleItem,
    onClick: (ScheduleItem) -> Unit
) {
    // Format tanggal mulai
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Parsing the date format
    val parsedDate = try {
        dateFormat.parse(startDate)
    } catch (e: Exception) {
        null
    }

    val formattedDate = if (parsedDate != null) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsedDate)
    } else {
        "Invalid Date"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(
                Color(0xFFF6F6F6),
            )
            .padding(16.dp)
            .clickable { onClick(scheduleItem) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color(0xFFF6F6F6),
                    shape = CircleShape
                ) // Yellow background for the circle
                .border(2.dp, Color.Black, shape = CircleShape) // Border around the circle
        ) {
            Icon(
                imageVector = Icons.Filled.Assignment,
                contentDescription = "Surat Pengembangan Icon",
                modifier = Modifier.fillMaxSize() .padding(5.dp),
                tint = Color.Black
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
                "testing" -> "Tahap Pengujian"
                else -> "Status Tidak Dikenali" // Default for other statuses
            }

            // Row to place Status and Date side by side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Space between the status and date
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = statusText,
                    fontSize = 14.sp,
                    color = when (status) {
                        "developed" -> kuning
                        "finished" -> ijo
                        "testing" -> ijo
                        else -> Color.Black
                    }
                )
                Text(
                    text = " $formattedDate",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
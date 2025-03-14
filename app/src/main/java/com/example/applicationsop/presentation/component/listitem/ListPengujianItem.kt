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
import androidx.compose.material.icons.filled.Assessment
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
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ListPengujianItem(
    perangkat_lunak: String,
    versiPerangkat: String,
    tujuanPengujian: String,
    metodePengujian: String,
    tanggalPengujian: String, // Menggunakan tanggalPengujian
    pelaksanaPengujian: String,
    status: String,
    onClick: () -> Unit // Fungsi untuk menangani klik
) {
    // Format tanggal pengujian
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Parsing the date format
    val parsedDate = try {
        dateFormat.parse(tanggalPengujian)
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
                Color(0xFFF6F6F6)
            )
            .padding(16.dp)
            .clickable { onClick() }, // Menambahkan aksi klik pada seluruh item
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular icon (human icon) with border
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color(0xFFF6F6F6),
                    shape = CircleShape
                )
                .border(2.dp, Color.Black, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Assessment,
                contentDescription = "Pengujian Iocn",
                modifier = Modifier.fillMaxSize() .padding(5.dp),
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = perangkat_lunak,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))

            Divider(
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Row to place status and date side by side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Space between the status and date
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (status) {
                        "testing" -> "Waiting for Approval"
                        "finished" -> "Pengujian Selesai"
                        else -> status // Default fallback in case of other statuses
                    },
                    fontSize = 14.sp,
                    color = when (status) {
                        "testing" -> kuning  // Yellow color for testing status
                        "finished" -> ijo  // Green color for finished status
                        else -> Color.Black
                    }
                )
                Text(
                    text = formattedDate, // Display the formatted date
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

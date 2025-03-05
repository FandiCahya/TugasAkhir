package com.example.applicationsop.data

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning

// Data class untuk menyimpan informasi detail usulan
data class DetailInfo(
    val id: String, // Tambahkan ID untuk identifikasi yang unik
    val namaSistem: String = "",
    val tanggal: String = "",
    val jenisSistem: String = "",
    val rencanaAnggaran: String = "",
    val masalahSistem: String = "",
    val outputHasil: String = "",
    var status: String = "",
    var alasan_penolakan: String = ""
)

// Data model for the schedule
data class ScheduleItem(
    val task: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val stage: String,
    val progressPercentage: Int // Ensure this is an Int
)

// Data class for storing detailed information
data class DetailPengujian(
    val id: String, // Unique ID for identification
    val namaSistem: String = "",
    val versiPerangkat: String = "", // Add this field
    val tujuanPengujian: String = "", // Add this field
    val metodePengujian: String = "", // Add this field
    val tanggalPengujian: String = "", // Add this field
    val pelaksanaPengujian: String = "", // Add this field
    var status: String = ""
)


@Composable
fun ListPengajuanItem(
    namaSistem: String,
    tanggal: String,
    jenisSistem: String,
    rencanaAnggaran: String,
    masalahSistem: String,
    outputHasil: String,
    status: String,
    alasan_penolakan: String,
    onClick: () -> Unit // Fungsi untuk menangani klik
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .background(
                Color(0xFFF6F6F6),
                RoundedCornerShape(20.dp)
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
                    kuning,
                    shape = CircleShape
                )
                .border(2.dp, Color.Black, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile Icon",
                modifier = Modifier.fillMaxSize(),
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = namaSistem,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))

            Divider(
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = status,
                fontSize = 14.sp,
                color = when (status) {
                    "pending" -> kuning
                    "rejected" -> abang
                    "accepted" -> ijo
                    else -> Color.Black
                }
            )
        }
    }
}


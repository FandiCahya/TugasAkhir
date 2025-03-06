package com.example.applicationsop.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProgressCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    navController: NavController
) {

    // Mapping untuk menampilkan "Pengembangan" atau "Pengujian"
    val displayTitle = when (title) {
        "PengembanganAdmin" -> "Pengembangan" // Menampilkan hanya "Pengembangan"
        "PengujianAdmin" -> "Pengujian" // Menampilkan hanya "Pengujian"
        else -> title // Jika tidak ditemukan, tampilkan title apa adanya
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { // Menambahkan aksi klik untuk navigasi
                if (title == "Pengembangan Admin") {
                    navController.navigate("pengembanganAdmin") // Arahkan ke halaman pengembangan
                } else if (title == "Pengujian Admin") {
                    navController.navigate("pengujianAdmin") // Arahkan ke halaman pengujian
                } else if (title == "Pengembangan User") {
                    navController.navigate("pengembanganUser") // Arahkan ke halaman pengembangan
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(10.dp) // Bayangan pada card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(25.dp), // Padding di dalam card
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = Color.White // Mengatur warna ikon menjadi putih
            )
            Spacer(modifier = Modifier.width(16.dp)) // Memberikan jarak antara ikon dan teks
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White // Mengatur warna teks menjadi putih
            )
        }
    }
}
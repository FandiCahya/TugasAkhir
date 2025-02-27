package com.example.applicationsop.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

@Composable
fun ProgressCardRiwayat(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp), // Padding vertikal antar card
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(10.dp) // Bayangan pada card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp), // Padding di dalam card
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Teks di tengah
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White // Mengatur warna teks menjadi putih
            )

            // Ikon diletakkan di sebelah kanan teks
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 5.dp),
                tint = Color.White // Mengatur warna ikon menjadi putih
            )
        }
    }
}
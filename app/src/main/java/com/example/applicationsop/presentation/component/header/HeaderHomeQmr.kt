package com.example.applicationsop.presentation.component.header

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeaderHomeQmr(
    navController: NavController,
    adminName: String?,
    adminToken: String?,
    adminuserId: String?,
    adminrole: String?,
    adminemail: String?,
    admindevisi: String?
) {
    // Ambil waktu saat ini
    val currentTime = LocalTime.now()
    val greeting = when {
        currentTime.isBefore(LocalTime.of(11, 0)) -> "Selamat Pagi"
        currentTime.isBefore(LocalTime.of(16, 0)) -> "Selamat Siang"
        currentTime.isBefore(LocalTime.of(19, 0)) -> "Selamat Sore"
        else -> "Selamat Malam"  // Mulai jam 19
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Maroon, Color.Transparent), // Gradasi dari Maroon ke Transparan
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Membuat teks dan ikon berada di ujung kiri dan kanan
            verticalAlignment = Alignment.CenterVertically // Agar teks dan ikon sejajar secara vertikal
        ) {
            // Menampilkan ucapan sesuai waktu
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = greeting, // Menampilkan ucapan berdasarkan waktu
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Putih
                )
                Text(
                    text = "Hi, ${adminName ?: "Kepala Cabang"}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Putih
                )
            }

            // Ikon profil di sebelah kanan
            IconButton(onClick = {
                // Arahkan ke menu profil ketika ikon diklik
//                navController.navigate("profile")
                navController.navigate("profile?token=$adminToken&userId=$adminuserId&role=$adminrole&name=$adminName&email=$adminemail&devisi=$admindevisi")


            }) {
                Icon(
                    imageVector = Icons.Filled.Person, // Menggunakan ikon "Person" dari Material Icons
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape) // Membuat border lingkaran putih
                )
            }
        }

        // Search Bar (tetap berada di bawah teks dan ikon)
        Spacer(modifier = Modifier.height(10.dp))
    }
}
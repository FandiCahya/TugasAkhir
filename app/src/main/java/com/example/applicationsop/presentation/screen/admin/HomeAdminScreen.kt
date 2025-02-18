package com.example.applicationsop.presentation.screen.admin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.zIndex
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.PinkPudar
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.kuning
import androidx.navigation.NavController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeAdminScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
    ) {
        // Konten utama, termasuk Header, Sections, dan lainnya
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Memberikan ruang bawah agar FAB tidak tertutup
        ) {
            // Header
            HeaderComposable(navController = navController)

            // Pengajuan Section
            SectionTitle("Pengajuan")
            SubmissionSection(navController = navController)

            Spacer(modifier = Modifier.height(16.dp))

            // Garis tengah
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 25.dp)
            )

            // Progres Section
            SectionTitle("Progres")
            ProgressSection(navController = navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeaderComposable(navController: NavController) {
    // Ambil waktu saat ini
    val currentTime = LocalTime.now()
    val greeting = when {
        currentTime.isBefore(LocalTime.NOON) -> "Selamat Pagi"
        currentTime.isBefore(LocalTime.of(18, 0)) -> "Selamat Siang"
        currentTime.isBefore(LocalTime.of(19, 0)) -> "Selamat Sore"
        else -> "Selamat Malam"
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
                    text = "Hi, Admin",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Putih
                )
            }

            // Ikon profil di sebelah kanan
            IconButton(onClick = {
                // Arahkan ke menu profil ketika ikon diklik
                navController.navigate("profile") // Anda perlu menambahkan rute "profile" di AppNavigation
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


@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 25.dp, vertical = 8.dp),
        color = Color.Black
    )
}

@Composable
fun SubmissionSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Kolom pertama - Card Butuh Konfirmasi (kuning) dengan ukuran lebih besar
        Column(
            modifier = Modifier
//                .fillMaxHeight() // Membuat kolom pertama memanjang ke bawah
                .weight(1f) // Membuat kolom pertama lebih besar
        ) {
            SubmissionCard(
                "Butuh Konfirmasi",
                color = kuning,
                icon = Icons.Filled.Timer,
                modifier = Modifier.height(180.dp),
                onClick = {
                    navController.navigate("list_pengajuanAdmin")
                }
            )
        }

        // Kolom kedua - Card Ditolak (merah) dan Card Dikembangkan (hijau)
        Column(
            modifier = Modifier
//                .fillMaxHeight() // Membuat kolom kedua memanjang ke bawah
                .weight(1f) // Membuat kolom kedua lebih besar dan seimbang
        ) {
            // Card "Ditolak" (merah)
            SubmissionCard("Ditolak", color = abang, icon = Icons.Filled.Close)

            Spacer(modifier = Modifier.height(16.dp)) // Memberikan jarak antara card

            // Card "Dikembangkan" (hijau) di bawah
            SubmissionCard("Dikembangkan", color = ijo, icon = Icons.Filled.Verified)
        }
    }
}

@Composable
fun SubmissionCard(
    text: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(150.dp) // Menentukan lebar card, agar tidak terlalu besar
            .padding(4.dp) // Padding antar card
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp), // Sudut card bulat
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(10.dp) // Bayangan pada card untuk efek 3D
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(30.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProgressSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Memberikan padding horizontal pada ProgressSection
    ) {
        // Menampilkan beberapa ProgressCard
        ProgressCard("Pengembangan", Icons.Filled.Timer, Maroon, navController)
        ProgressCard("Pengujian", Icons.Filled.History, Maroon, navController)

        // Garis tengah
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(
                horizontal = 5.dp,
                vertical = 10.dp
            ) // Berikan ruang kiri dan kanan
        )

        // Card Riwayat dengan Icon di bawah
        ProgressCardRiwayat("Riwayat", Icons.Filled.History, Maroon)
    }
}

@Composable
fun ProgressCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { // Menambahkan aksi klik untuk navigasi
                if (title == "Pengembangan") {
                    navController.navigate("pengembanganAdmin") // Arahkan ke halaman pengembangan
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
package com.example.applicationsop.presentation.component

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderWithSearch(
    navController: NavController,
    title: String, // Dynamically change the title for each screen
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Maroon, Color.Transparent), // Gradasi dari Maroon ke Transparan
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .zIndex(1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(
                navController = navController,
                colorVersion = "w",
            )

            Spacer(modifier = Modifier.width(80.dp)) // Space between BackButton and the title

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Search bar positioned below the title and center it vertically
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Align the search bar at the bottom and center horizontally
                .padding(horizontal = 50.dp)
                .padding(bottom = 30.dp)
        ) {
            TextField(
                value = "",
                onValueChange = { /* Handle text input here */ },
                placeholder = {
                    Text(
                        text = "Cari",
                        color = Color.Gray, // Adjust color as needed
                        style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Maroon,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,  // Remove the focus indicator line
                    unfocusedIndicatorColor = Color.Transparent // Remove the unfocused indicator line
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeaderHomeAdmin(
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
                    text = "Hi, ${adminName ?: "Admin"}",
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeaderHomeUser(navController: NavController, nameUser: String?, TokenUser: String?, userIdUser: String?, roleUser: String?, emailUser: String?, devisiUser: String?) {

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
                    text = "Hi, ${nameUser ?: "Mutant"}",  // Menampilkan nama user atau "Mutant" jika tidak ada nama
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Putih
                )
            }

            // Ikon profil di sebelah kanan
            IconButton(onClick = {
                // Arahkan ke menu profil ketika ikon diklik
                navController.navigate("profile?token=$TokenUser&userId=$userIdUser&role=$roleUser&name=$nameUser&email=$emailUser&devisi=$devisiUser") // Anda perlu menambahkan rute "profile" di AppNavigation
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
fun HeaderFormUsulan(title: String, navController: NavController) {
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
            .padding(16.dp)
    ) {
        // Header with Back Button and Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, // Vertically align the items
            horizontalArrangement = Arrangement.Start // Align items to the start (left)
        ) {
            BackButton(
                navController = navController,
                colorVersion = "w"
            )  // Back Button on the left

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Putih,
                textAlign = TextAlign.Center // Ensure the title is centered
            )
        }
    }
}

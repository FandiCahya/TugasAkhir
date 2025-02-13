package com.example.applicationsop.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.ui.theme.Maroon // Import the Maroon color from your theme
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import java.time.format.TextStyle
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rectangle1217(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
//            .clip(shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Maroon, Color.Transparent), // Gradasi dari Maroon ke Transparan
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .zIndex(1f)
    ) {
        // Back Button on the left
        BackButton(
            navController = navController,
            colorVersion = "w"
        )

        // Title Text "Pengajuan" aligned in the center
        Text(
            text = "Pengajuan",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 90.dp)
        )

        // Search bar positioned below "Pengajuan" text and center it vertically
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

@Composable
fun ListPengajuanItem(namaSistem: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .background(
                Color(0xFFF6F6F6),
                RoundedCornerShape(20.dp)
            ) // Set the background to light gray
            .padding(16.dp),
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

            Text(
                text = status,
                fontSize = 14.sp,
                color = when (status) {
                    "Menunggu konfirmasi" -> kuning // Ganti dengan warna Maroon dari tema Anda
                    "Pengujian ditolak" -> abang // Ganti dengan warna abang
                    "Pengajuan Diterima" -> ijo // Ganti dengan warna hijau dari tema Anda
                    else -> Color.Black // Default jika status tidak dikenali
                }
            )
        }
    }
}

@Composable
fun ListPengajuanScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Set the background color to white
    ) {

        // Header with back button and search icon
        Rectangle1217(navController = navController)
        Spacer(modifier = Modifier.height(20.dp))

        // List of submissions
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(2) { index ->
                ListPengajuanItem(
                    namaSistem = "Nama Sistem ${index + 1}",
                    status = if (index % 2 == 0) "Menunggu konfirmasi" else "Menunggu Konfirmasi"
                )
            }
        }
    }
}



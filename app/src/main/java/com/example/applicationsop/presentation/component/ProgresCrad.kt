package com.example.applicationsop.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.example.applicationsop.ui.theme.PinkPudar

@Composable
fun ProgressCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    count: Int? = null,
    navController: NavController
) {

    // Mapping untuk menampilkan "Pengembangan" atau "Pengujian"
    val displayTitle = when (title) {
        "Pengembangan Admin" -> "Pengembangan"
        "Pengujian Admin" -> "Pengujian"
        "Pengembangan User" -> "Pengembangan"
        "Pengujian User" -> "Pengujian"
        "Pengembangan Kacab" -> "Pengembangan"
        "Pengembangan Qmr" -> "Pengembangan"
        else -> title
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable {
                when (title) {
                    "Pengembangan Admin" -> navController.navigate("pengembanganAdmin")
                    "Pengujian Admin" -> navController.navigate("pengujianAdmin")
                    "Pengembangan User" -> navController.navigate("pengembanganUser")
                    "Pengujian User" -> navController.navigate("pengujianUser")
                    "Pengembangan Kacab" -> navController.navigate("pengembanganKacab")
                }
            }
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = color),
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(25.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = displayTitle,
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = displayTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Menampilkan badge notifikasi jika count > 0
        if ((count ?: 0) > 0) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-10).dp) // Posisi badge
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = PinkPudar),
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), // Pastikan Box mengisi seluruh Card
                        contentAlignment = Alignment.Center // Teks berada di tengah
                    ) {
                        Text(
                            text = count.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
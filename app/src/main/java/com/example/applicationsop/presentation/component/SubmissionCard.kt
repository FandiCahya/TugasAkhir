package com.example.applicationsop.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.ui.theme.PinkPudar

@Composable
fun SubmissionCard(
    color: Color,
    icon: ImageVector,
    label: String = "",
    modifier: Modifier = Modifier,
    count: Int? = null,
    onClick: () -> Unit = {}
) {
    // Bungkus seluruh kartu dengan Column dan klik
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .width(64.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(48.dp)) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = color),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp) // Ikon lebih kecil
                    )
                }
            }

            // Notifikasi badge
            if ((count ?: 0) > 0) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = PinkPudar),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if ((count ?: 0) > 99) "99+" else count.toString(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                // angkat teks sedikit ke atas
                                modifier = Modifier.offset(y = (-1).dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Label teks di bawah ikon
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color(0xFF444444),
            maxLines = 1
        )
    }
}

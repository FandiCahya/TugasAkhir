package com.example.applicationsop.presentation.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.PinkPudar

@Composable
fun SubmissionCard(
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    count: Int? = null, // Default null agar indikator tidak tampil jika count tidak diset
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(4.dp)
    ) {
        Card(
            modifier = Modifier
                .size(80.dp)
                .clickable(onClick = onClick),
            shape = CircleShape, // Bentuk kartu menjadi bulat
            colors = CardDefaults.cardColors(containerColor = color),
            elevation = CardDefaults.cardElevation(10.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
            }
        }

        // Tambahkan indikator notifikasi jika count > 0
        if ((count ?: 0) > 0) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.TopEnd) // Meletakkan badge di pojok kanan atas
                    .background(Color.Transparent)
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = PinkPudar),
                    modifier = Modifier.size(20.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = (count ?: 0).toString(),
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






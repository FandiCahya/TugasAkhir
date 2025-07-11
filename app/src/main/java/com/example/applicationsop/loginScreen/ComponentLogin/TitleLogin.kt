package com.example.applicationsop.loginScreen.ComponentLogin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.PinkPudar

@Composable
fun TitleTextComposable() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SIMPEL",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            color = Maroon,

            )
    }
}

@Composable
fun SubtitleTextComposable() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sistem Informasi Pengajuan Perangkat Lunak",
            fontSize = 13.sp,
            color = PinkPudar,
            modifier = Modifier.padding(bottom = 180.dp)
        )
    }
}
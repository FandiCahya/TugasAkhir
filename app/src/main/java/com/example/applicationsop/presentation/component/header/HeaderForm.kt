package com.example.applicationsop.presentation.component.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih

@Composable
fun HeaderForm(title: String, navController: NavController) {
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BackButton(
                navController = navController,
                colorVersion = "w"
            )

            Spacer(modifier = Modifier.padding(horizontal = 30.dp))

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Putih,
                textAlign = TextAlign.Center,
            )
        }
    }
}
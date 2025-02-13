package com.example.applicationsop.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.NavController
import com.example.applicationsop.ui.theme.Maroon // Import the Maroon color from your theme

@Composable
fun BackButton(navController: NavController, colorVersion: String = "default") {
    // Set background color and icon tint based on version
    val backgroundColor = when (colorVersion) {
        "m" -> Maroon // Use Maroon from your UI theme for background
        "w" -> Color.White // Use white for background
        else -> Color.Gray // Default color
    }

    val iconTint = when (colorVersion) {
        "m" -> Color.White // Icon color is white when background is Maroon
        "w" -> Maroon // Icon color is Maroon when background is white
        else -> Color.Black // Default icon color
    }

    IconButton(
        onClick = {
            navController.popBackStack() // This will navigate back to the previous screen
        },
        modifier = Modifier
            .size(40.dp) // Set the button size
            .background(backgroundColor, CircleShape) // Set circle background color
            .padding(10.dp) // Padding inside the button
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.size(24.dp), // Set icon size
            tint = iconTint // Set icon color based on the background
        )
    }
}

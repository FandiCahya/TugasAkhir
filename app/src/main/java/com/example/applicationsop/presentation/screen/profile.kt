package com.example.applicationsop.presentation.screen

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import kotlin.io.path.Path

@Composable
fun ProfileScreen(navController: NavController) {
    // Menyimpan tanda tangan
    var signaturePath by remember { mutableStateOf(androidx.compose.ui.graphics.Path()) }
    var showSignaturePad by remember { mutableStateOf(false) }
    var showSignatureValidDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih) // Background set to white
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Back to Home",
                fontSize = 18.sp,
                color = Color.Black
            )
        }

        // Gradient Background for Profile Info Section
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Adjust the height of the profile section
                .background(brush = Brush.linearGradient(
                    0.26f to Color(0xffec786b), // Start color
                    1f to Color.White, // End color
                    start = Offset(96.01f, -11.15f),
                    end = Offset(187.5f, 276f)
                ))
                .padding(5.dp)
                .shadow(8.dp, shape = RoundedCornerShape(16.dp)) // Shadow for card
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Picture (Left)
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.Gray, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person, // Using the "Person" icon
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(100.dp), // Icon size
                        tint = Color.White // White icon color
                    )
                }

                // User Details (Right)
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .weight(1f) // Make sure this column takes the remaining space
                        .fillMaxHeight(), // Fill the height of the profile box
                    verticalArrangement = Arrangement.Center // Center content vertically
                ) {
                    Text(
                        text = "Boys", // User's name
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp)) // Space between name and email
                    Text(
                        text = "ahmadakakom@gmail.com", // User's email
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp)) // Space between email and phone number
                    Text(
                        text = "08981235676", // User's phone number
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            // Edit Button in the top-right corner of the profile card
            IconButton(
                onClick = { /* Handle Edit */ },
                modifier = Modifier
                    .align(Alignment.TopEnd) // Align the button to the top-right
                    .padding(8.dp) // Padding from the corner
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit Profile",
                    tint = Maroon
                )
            }
        }

        // Menu Section (History and Sign Out Buttons)
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // History Button
            Button(
                onClick = { /* Navigate to history screen */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Putih)
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "History",
                    modifier = Modifier.size(24.dp),
                    tint = Maroon
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "History",
                    color = Maroon,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sign Out Button
            Button(
                onClick = { /* Handle Sign Out */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Putih)
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Sign Out",
                    modifier = Modifier.size(24.dp),
                    tint = Maroon
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign Out",
                    color = Maroon,
                    fontSize = 16.sp
                )
            }
        }
    }
}




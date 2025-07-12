package com.example.applicationsop.presentation.screen

import android.content.Context
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.Api.logoutUser
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@Composable
fun ProfileUser(
    navController: NavController,
    token: String?,
    userId: String?,
    role: String?,
    name: String?,
    email: String?,
    devisi: String?
) {
    var signaturePath by remember { mutableStateOf(androidx.compose.ui.graphics.Path()) }
    var showSignaturePad by remember { mutableStateOf(false) }
    var showSignatureValidDialog by remember { mutableStateOf(false) }

    var isLoggingOut by remember { mutableStateOf(false) }

    // Ambil context untuk menampilkan Toast
    val context = LocalContext.current

    // LaunchedEffect to trigger logout when token is non-null
    LaunchedEffect(isLoggingOut) {
        if (isLoggingOut && token != null) {
            try {
                val success = logoutUser(token) // Perform logout
                if (success) {
                    // Menampilkan Toast ketika logout berhasil
                    clearUserData(context)
                    Toast.makeText(context, "Logout Berhasil", Toast.LENGTH_SHORT).show()

                    // Navigate to login after successful logout
                    navController.navigate("login") {
                        // Clear the back stack
                        popUpTo(0) { inclusive = true }
                    }
                    println("Logout Successfully")
                } else {
                    // Handle logout failure
                    println("Logout failed")
                    println("Token: $token")
                    Toast.makeText(context, "Logout Gagal", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Tangani error jika logoutUser gagal
                println("Error during logout: ${e.message}")
                Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Maroon, Color.Transparent),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
                .padding(20.dp)
        ) {
            BackButton(
                navController = navController,
                colorVersion = "w"
            )

            Text(
                text = "Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 165.dp) // Adjust the position of the text if needed
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)
                .padding(top = 100.dp)
                .shadow(8.dp, shape = RoundedCornerShape(15.dp))
                .zIndex(1f)
                .background(Color.White)
        ) {
            // Profile Section (Picture + User Details)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Padding for the row
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Picture
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.Gray, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person, // Person icon
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(100.dp), // Icon size
                        tint = Color.White // Icon color
                    )
                }

                // User Details (Name, Email, Phone)
                Spacer(modifier = Modifier.width(20.dp))
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center // Vertically center content
                ) {
                    Text(
                        text = "${name ?: "mutant"}", // User's name
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp)) // Space between name and email
                    Text(
                        text = "${email ?: "admin@gmail.com"}", // User's email
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp)) // Space between email and phone number
                    Text(
                        text = "${devisi ?: "VAS"}", // User's phone number
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            // Menu Section (Buttons for History and Sign Out)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp) // Adjust the top padding to place the menu section below the profile
            ) {

                // Sign Out Button
                TextButton(
                    onClick = {
                        // Set flag to start logging out
                        isLoggingOut = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                    // Removed the vertical padding to bring the content closer to the divider
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Aligns content to the left
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Sign Out",
                            modifier = Modifier.size(24.dp),
                            tint = Maroon
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
                        Text(
                            text = "Sign Out",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }
                }

                // Divider after Sign Out button (No extra padding)
                Divider(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(bottom = 30.dp)
                )

            }
        }
    }
}

fun clearUserData(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    sharedPreferences.edit().clear().apply()
    println("✅ Semua data di SharedPreferences telah dibersihkan.")
    editor.clear() // Clear all stored data
    editor.apply() // Commit the changes
}

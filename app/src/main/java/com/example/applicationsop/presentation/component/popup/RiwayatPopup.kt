package com.example.applicationsop.presentation.component.popup

import android.content.Context
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.biru
import generatePDF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun RiwayatPopup(
    onDismiss: () -> Unit,
    navController: NavController,
    id: String,
    namaSistem: String,
    versiPerangkat: String,
    tujuanPengujian: String,
    metodePengujian: String,
    tanggalPengujian: String,
    pelaksanaPengujian: String,
    status: String
) {
    // Get the current context
    val context = LocalContext.current

    // Dummy data
    val id = "12345"
    val namaSistem = "Sistem Pengujian A"
    val versiPerangkat = "v1.2.0"
    val tujuanPengujian = "Menguji kestabilan sistem"
    val metodePengujian = "Black Box Testing"
    val tanggalPengujian = "2025-03-21"
    val pelaksanaPengujian = "John Doe"
    val status = "finished"
    val persetujuanId = "54321"

    // Define directory for saving the PDF
    val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyAppDocs")
    if (!directory.exists()) {
        directory.mkdirs() // Create directory if it doesn't exist
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)) // Gelapkan background
                .clickable { onDismiss() } // Menutup popup jika area gelap di klik
        )

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(20.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Icon and Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = "Timer Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Maroon
                    )
                }
                Text(
                    text = "Riwayat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 130.dp)
                )

                // Line separator
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Content
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Rows for data...
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(70.dp, Alignment.CenterHorizontally)
                ) {
                    // Tombol Download
                    Button(
                        onClick = {
                            // Ensure directory exists
                            val directory = context.getExternalFilesDir(null) // Now we use LocalContext.current

                            // Call the generatePDF function with all the required parameters inside a coroutine scope
                            if (directory != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    generatePDF(
                                        context,
                                        directory,
                                        id,
                                        namaSistem,
                                        versiPerangkat,
                                        tujuanPengujian,
                                        metodePengujian,
                                        tanggalPengujian,
                                        pelaksanaPengujian,
                                        status
                                    )
                                }
                            }
                        },
                        content = {
                            Text("Download")
                        }
                    )

                    // Tombol Show Laporan
                    Button(
                        onClick = {
                            // Navigasi ke halaman laporan
                            navController.navigate("show_laporan_screen?id=$id")
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = biru),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Laporan", color = Color.White)
                    }
                }
            }
        }
    }
}


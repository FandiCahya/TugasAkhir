package com.example.applicationsop.presentation.component.popup

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
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.data.DetailPengujian
import com.example.applicationsop.models.Catatan
import com.example.applicationsop.models.GetPengujianDetail
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Purple40
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.biru
import com.example.applicationsop.ui.theme.ijo

@Composable
fun DetailPopupPengujianPemohon(
    onDismiss: () -> Unit,
    id: String,
    namaSistem: String,
    versiPerangkat: String,
    tujuanPengujian: String,
    metodePengujian: String,
    tanggalPengujian: String,
    pelaksanaPengujian: String,
    status: String,
    persetujuanId: String,
    navController: NavController // Menambahkan navController sebagai parameter
) {
    // Cetak ID untuk debugging
    println("ID Pengujian: $id")

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
                    text = "Informasi Pengujian",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 110.dp)
                )

                // Line separator
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Content
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Row for Nama Sistem
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Nama Perangkat",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $namaSistem",
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Row for Versi Perangkat
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Versi", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $versiPerangkat",
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Row for Tujuan Pengujian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tujuan", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $tujuanPengujian",
                                color = Color.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Row for Metode Pengujian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Metode", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $metodePengujian",
                                color = Color.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Row for Tanggal Pengujian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tanggal", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $tanggalPengujian",
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Row for Pelaksana Pengujian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Pelaksana", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $pelaksanaPengujian",
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status Approval
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Status", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $status",
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally) // Menyebar tombol dengan jarak yang sama
                    ) {
                        // Tombol See All
                        Button(
                            onClick = {
                                navController.navigate("DetailPengujianPemohon?id=$id&namaSistem=$namaSistem")
                                {
                                    popUpTo("DetailPengujianPemohon") { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .width(100.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("See All", color = Color.White)
                        }

                        // Tombol Approval
                        Button(
                            onClick = {  // Example: Navigating to DetailApproval with a mock DetailPengujian object
                                navController.navigate("detail_approval_screen?persetujuanId=$persetujuanId") },
                            modifier = Modifier
                                .width(100.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = biru),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Approval", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

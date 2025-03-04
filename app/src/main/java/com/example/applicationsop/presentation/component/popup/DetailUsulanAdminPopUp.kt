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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.updatePengajuan
import com.example.applicationsop.models.PengajuanRequest
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import kotlinx.coroutines.launch

@Composable
fun DetailPopupUsulanAdmin(
    onDismiss: () -> Unit,
    id: String,
    hariTanggal: String,
    namaSistem: String,
    jenisSistem: String,
    rencanaAnggaran: String,
    masalahSistem: String,
    outputHasil: String,
    status: String,
    alasan: String? = null, // Alasan hanya ada jika status ditolak
    isAdmin: Boolean, // Menambahkan parameter untuk memeriksa peran
    onAcceptClick: () -> Unit, // Fungsi untuk menerima usulan
    onRejectClick: (String) -> Unit, // Fungsi untuk menolak usulan
    navController: NavController
) {
//    println("Idnya Adalah: $id")
    var inputAlasan by remember { mutableStateOf(alasan.orEmpty()) }
    var showAlasanInput by remember { mutableStateOf(false) }
    // Coroutine scope for launching suspend functions
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)) // Gelapkan background
                .clickable { onDismiss() } // Menutup popup jika area gelap di klik
        )

        // Card Popup
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
                        imageVector = Icons.Filled.Description,
                        contentDescription = "Des Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Maroon
                    )
                }
                Text(
                    text = "Detail Usulan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 110.dp)
                )

                // Line separator
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Content
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Content with two columns for title and content
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Row for Hari/Tanggal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Kolom 1 (Judul)
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Tanggal",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            // Kolom 2 (Isi)
                            Column(
                                modifier = Modifier.weight(2f)
                            ) {
                                Text(": $hariTanggal", color = Color.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Row for Nama Sistem
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Kolom 1 (Judul)
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Nama Sistem",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            // Kolom 2 (Isi)
                            Column(
                                modifier = Modifier.weight(2f)
                            ) {
                                Text(": $namaSistem", color = Color.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Row for Jenis Sistem
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Kolom 1 (Judul)
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Jenis", fontWeight = FontWeight.Bold, color = Color.Black)
                            }

                            // Kolom 2 (Isi)
                            Column(
                                modifier = Modifier.weight(2f)
                            ) {
                                Text(": $jenisSistem", color = Color.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Row for Rencana Anggaran
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Kolom 1 (Judul)
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Rencana Anggaran",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            // Kolom 2 (Isi)
                            Column(
                                modifier = Modifier.weight(2f)
                            ) {
                                Text(": $rencanaAnggaran", color = Color.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Row for Masalah pada Sistem
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Kolom 1 (Judul)
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Masalah pada sistem",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            // Kolom 2 (Isi)
                            Column(
                                modifier = Modifier.weight(2f)
                            ) {
                                Text(": $masalahSistem", color = Color.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Row for Output/Hasil yang Diharapkan
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Kolom 1 (Judul)
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Output/Hasil yang diharapkan",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            // Kolom 2 (Isi)
                            Column(
                                modifier = Modifier.weight(2f)
                            ) {
                                Text(": $outputHasil", color = Color.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Row for Status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Kolom 1 (Judul)
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Status", fontWeight = FontWeight.Bold, color = Color.Black)
                            }

                            // Kolom 2 (Isi)
                            Column(
                                modifier = Modifier.weight(2f)
                            ) {
                                Text(": $status", color = Color.Black)
                            }
                        }
                    }
                }

                // Line separator
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Menampilkan tombol "Terima" dan "Tolak" hanya jika statusnya "pending" atau "rejected"
                if (status == "pending" || status == "rejected") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                // Call the updatePengajuan API when "Terima" button is clicked
                                val pengajuanRequest = PengajuanRequest(
                                    status = "accepted" // Only update the status field
                                )

                                // Call updatePengajuan API to update the status to accepted
                                coroutineScope.launch {
                                    try {
                                        val response = updatePengajuan(id, pengajuanRequest)
                                        if (response.status.value in 200..299) {
                                            onAcceptClick() // Execute the callback after success
                                            onDismiss()
                                        } else {
                                            println("Failed to update status")
                                        }
                                    } catch (e: Exception) {
                                        println("Error: ${e.message}")
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(120.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = ijo),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Terima", color = Color.White)
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = {
                                showAlasanInput = true // Show the input for reason
                            },
                            modifier = Modifier
                                .width(120.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = abang),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Tolak", color = Color.White)
                        }
                    }

                    // Tampilkan input alasan jika tombol Tolak ditekan
                    if (showAlasanInput) {
                        // Input alasan
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                        ) {
                            Text("Alasan Penolakan", fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = inputAlasan,
                                onValueChange = { inputAlasan = it },
                                placeholder = { Text("Masukkan alasan") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Button to submit the rejection reason
                            Button(
                                onClick = {
                                    if (inputAlasan.isNotEmpty()) {
                                        // Call the onRejectClick function with the inputAlasan value
                                        onRejectClick(inputAlasan)
                                        onDismiss()
                                    }
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = ButtonDefaults.buttonColors(containerColor = abang),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Kirim Alasan", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
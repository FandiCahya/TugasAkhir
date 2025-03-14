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
import com.example.applicationsop.models.Pengajuan
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
    var inputAlasan by remember { mutableStateOf(alasan.orEmpty()) }
    var showAlasanInput by remember { mutableStateOf(false) }
    var pengajuanList by remember { mutableStateOf<List<Pengajuan>>(emptyList()) }

    // Coroutine scope for launching suspend functions
    val coroutineScope = rememberCoroutineScope()

    val displayNames = mapOf(
        "sistem_baru" to "Sistem Baru",
        "pengembangan" to "Pengembangan",
        "termasuk_dalam_perencanaan" to "Termasuk Anggaran",
        "tidak_termasuk_perencanaan" to "Tidak Termasuk Anggaran"
    )

    val jenisSistemDisplay = displayNames[jenisSistem] ?: jenisSistem
    val rencanaAnggaranDisplay = displayNames[rencanaAnggaran] ?: rencanaAnggaran

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
                                Text(": $jenisSistemDisplay", color = Color.Black)
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
                                Text(": $rencanaAnggaranDisplay", color = Color.Black)
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

                // Tombol berdasarkan status
                if (status == "accepted") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        horizontalArrangement = Arrangement.End // Menempatkan tombol di sebelah kanan
                    ) {
                        Button(
                            onClick = {
                                // Redirect ke halaman "Add Schedule" dengan parameter id dan namaSistem
                                navController.navigate("addSchedule?id=$id&namaSistem=$namaSistem")
                            },
                            modifier = Modifier
                                .width(130.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = ijo),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Add Schedule", color = Color.White)
                        }
                    }
                } else if (status == "rejected") {
                    // Tombol "Tutup" untuk status "rejected"
                    Row(
                        modifier = Modifier.fillMaxWidth(), // Take full width
                        horizontalArrangement = Arrangement.End // Align to the right
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .padding(end = 16.dp) // Optional padding to give some space from the edge
                                .width(100.dp)
                                .shadow(
                                    4.dp,
                                    RoundedCornerShape(16.dp)
                                ), // Set the width of the button to a smaller size
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Tutup",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    // Tombol Terima dan Tolak untuk status selain "accepted" atau "rejected"
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
                                            // Remove the accepted pengajuan from the list
                                            pengajuanList = pengajuanList.filterNot { it.id == id }

                                            // Execute the callback after success
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
                                // Show the reason input for rejection
                                showAlasanInput = true
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(
                                "Alasan Penolakan",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = inputAlasan,
                                onValueChange = { inputAlasan = it },
                                placeholder = { Text("Masukkan alasan") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (inputAlasan.isNotEmpty()) {
                                        // Prepare the PengajuanRequest for rejection with the reason
                                        val pengajuanRequest = PengajuanRequest(
                                            status = "rejected",  // Set status to rejected
                                            alasan_penolakan = inputAlasan  // Add the rejection reason
                                        )

                                        // Call updatePengajuan API to update the status to rejected
                                        coroutineScope.launch {
                                            try {
                                                val response = updatePengajuan(id, pengajuanRequest)
                                                if (response.status.value in 200..299) {
                                                    onRejectClick(inputAlasan)  // Execute the callback after rejection success
                                                    println("response success update alasan$response")
                                                    onDismiss()
                                                } else {
                                                    println("Failed to update status")
                                                }
                                            } catch (e: Exception) {
                                                println("Error: ${e.message}")
                                            }
                                        }
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
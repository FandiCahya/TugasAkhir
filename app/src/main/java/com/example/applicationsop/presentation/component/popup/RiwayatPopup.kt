package com.example.applicationsop.presentation.component.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.models.PersetujuanPengujianDetail
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.biru
import generatePDF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RiwayatPopup(
    onDismiss: () -> Unit,
    navController: NavController,
    id: String,
    tgl: String,
    namaSistem: String,
    jenis: String,
    rencanaAnggaran: String,
    masalah: String,
    output: String,
    tanggalMulai: String?,
    tanggalSelesai: String?,
    tahap: String?,
    keterangan: String?,
    perangkatLunak: String?,
    versiPerangkat: String?,
    tujuanPengujian: String?,
    metodePengujian: String?,
    detailPersetujuan: List<PersetujuanPengujianDetail>,
    status: String?
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable { onDismiss() }
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
            Column(modifier = Modifier.padding(16.dp)) {

                // Header
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
                    text = "Detail Riwayat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

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
                                "Tanggal Pengajuan",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $tgl",
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
                            Text("Nama Sistem", fontWeight = FontWeight.Bold, color = Color.Black)
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

                    // Row for Tujuan Pengujian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Jenis", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $jenis",
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
                            Text(
                                "Rencana Anggaran",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $rencanaAnggaran",
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
                            Text("Masalah", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $masalah",
                                color = Color.Black,
                                maxLines = 2,
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
                            Text("Output", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $output",
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(top = 8.dp))
            }

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
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

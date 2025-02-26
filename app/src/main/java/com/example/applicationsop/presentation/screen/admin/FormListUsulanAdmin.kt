package com.example.applicationsop.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.applicationsop.data.DetailInfo
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.presentation.component.HeaderWithSearch
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.models.Pengajuan
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import java.text.SimpleDateFormat
import java.util.Date

fun formatTanggal(tanggal: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
    val outputFormat = SimpleDateFormat("yyyy-MM-dd") // Format yang hanya menampilkan tanggal
    val date: Date = inputFormat.parse(tanggal)
    return outputFormat.format(date) // Mengembalikan tanggal yang diformat
}


@Composable
fun ListPengajuanItem(
    namaSistem: String,
    tanggal: String,
    jenisSistem: String,
    rencanaAnggaran: String,
    masalahSistem: String,
    outputHasil: String,
    status: String,
    onClick: () -> Unit // Fungsi untuk menangani klik
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .background(
                Color(0xFFF6F6F6),
                RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
            .clickable { onClick() }, // Menambahkan aksi klik pada seluruh item
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular icon (human icon) with border
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    kuning,
                    shape = CircleShape
                )
                .border(2.dp, Color.Black, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile Icon",
                modifier = Modifier.fillMaxSize(),
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = namaSistem,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))

            Divider(
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = status,
                fontSize = 14.sp,
                color = when (status) {
                    "pending" -> kuning
                    "rejected" -> abang
                    "accepted" -> ijo
                    else -> Color.Black
                }
            )
        }
    }
}

@Composable
fun ListPengajuanScreenAdmin(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedDetail by remember { mutableStateOf(DetailInfo(id="")) }
    var pengajuanList by remember { mutableStateOf<List<Pengajuan>>(emptyList()) }


    LaunchedEffect(Unit) {
        // Fetching the data when the Composable is first launched
        val fetchedPengajuanList = fetchPengajuanList() // Fetch the data
        pengajuanList = fetchedPengajuanList // Updating the state
//        println("Pengajuan List View :${pengajuanList}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with back button and search icon
        HeaderWithSearch(navController = navController, title = "Pengajuan")
        Spacer(modifier = Modifier.height(20.dp))

        // List of submissions
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(pengajuanList) { pengajuan ->
                // Using data from the API response dynamically
                ListPengajuanItem(
                    namaSistem = pengajuan.nama_sistem,
                    tanggal = pengajuan.tgl,
                    jenisSistem = pengajuan.jenis,
                    rencanaAnggaran = pengajuan.rencana_anggaran,
                    masalahSistem = pengajuan.masalah,
                    outputHasil = pengajuan.output,
                    status = pengajuan.status,
                    onClick = {
                        selectedDetail = DetailInfo(
                            id = pengajuan.id,
                            namaSistem = pengajuan.nama_sistem,
                            tanggal = pengajuan.tgl.toString(),
                            jenisSistem = pengajuan.jenis,
                            rencanaAnggaran = pengajuan.rencana_anggaran,
                            masalahSistem = pengajuan.masalah,
                            outputHasil = pengajuan.output,
                            status = pengajuan.status
                        )
                        showPopup = true
                    }
                )
            }
        }
    }

    if (showPopup) {
        DetailPopup(
            onDismiss = { showPopup = false },
            hariTanggal = selectedDetail.tanggal,
            namaSistem = selectedDetail.namaSistem,
            jenisSistem = selectedDetail.jenisSistem,
            rencanaAnggaran = selectedDetail.rencanaAnggaran,
            masalahSistem = selectedDetail.masalahSistem,
            outputHasil = selectedDetail.outputHasil,
            status = selectedDetail.status,  // Gunakan status yang dipilih secara dinamis
            alasan = if (selectedDetail.status == "Pengajuan ditolak") "Output kurang jelas" else null, // Alasan hanya muncul jika status ditolak
            isAdmin = true, // Menambahkan parameter isAdmin yang bisa ditentukan sesuai pengguna
            onAcceptClick = {
                // Aksi terima (ubah status atau lakukan tindakan lainnya)
            },
            onRejectClick = { alasan ->
                // Aksi tolak dengan alasan yang dimasukkan
            },
            navController = navController
        )
    }
}

@Composable
fun DetailPopup(
    onDismiss: () -> Unit,
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

    Box(modifier = Modifier.fillMaxSize()) {
        // Gelap di latar belakang
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

                // Button for Accept or Reject
                if (isAdmin) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Menampilkan tombol Terima dan Tolak hanya jika alasan belum diinput
                        if (!showAlasanInput) {
                            Button(
                                onClick = onAcceptClick,
                                modifier = Modifier
                                    .width(120.dp) // Menyesuaikan lebar tombol
                                    .shadow(
                                        4.dp,
                                        RoundedCornerShape(16.dp)
                                    ), // Menambahkan shadow pada tombol
                                colors = ButtonDefaults.buttonColors(containerColor = ijo),
                                shape = RoundedCornerShape(16.dp) // Membuat tombol dengan sudut yang membulat
                            ) {
                                Text("Terima", color = Color.White)
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Button(
                                onClick = {
                                    showAlasanInput = true
                                }, // Menampilkan input alasan jika Tolak ditekan
                                modifier = Modifier
                                    .width(120.dp) // Menyesuaikan lebar tombol
                                    .shadow(
                                        4.dp,
                                        RoundedCornerShape(16.dp)
                                    ), // Menambahkan shadow pada tombol
                                colors = ButtonDefaults.buttonColors(containerColor = abang),
                                shape = RoundedCornerShape(16.dp) // Membuat tombol dengan sudut yang membulat
                            ) {
                                Text("Tolak", color = Color.White)
                            }
                        }
                    }
                }

                // Tombol Add Schedule
                Button(
                    onClick = { navController.navigate("addSchedule") }, // Navigasi ke addSchedule
                    modifier = Modifier
                        .width(150.dp)
                        .padding(top = 6.dp)
                        .align(alignment = Alignment.End)
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Add Schedule", color = Color.White)
                }
            }
        }
    }

    // Input alasan jika ditolak
    if (showAlasanInput) {
        // Menampilkan TextField untuk alasan
        TextField(
            value = inputAlasan,
            onValueChange = { inputAlasan = it },
            label = { Text("Alasan") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        )

        // Tombol Kirim untuk mengirim alasan
        Button(
            onClick = { onRejectClick(inputAlasan) }, // Kirim alasan penolakan
            modifier = Modifier
                .width(120.dp)
//                .align(Alignment.End) // Menempatkan tombol di ujung kanan
                .shadow(4.dp, RoundedCornerShape(16.dp)), // Menambahkan shadow pada tombol
            colors = ButtonDefaults.buttonColors(containerColor = Maroon),
            shape = RoundedCornerShape(16.dp) // Membuat tombol dengan sudut yang membulat
        ) {
            Text("Kirim", color = Color.White)
        }
    }
}


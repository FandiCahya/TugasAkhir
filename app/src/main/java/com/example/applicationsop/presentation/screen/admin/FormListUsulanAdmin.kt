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
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rectangle1217(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
//            .clip(shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Maroon, Color.Transparent), // Gradasi dari Maroon ke Transparan
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .zIndex(1f)
    ) {
        // Back Button on the left
        BackButton(
            navController = navController,
            colorVersion = "w"
        )

        // Title Text "Pengajuan" aligned in the center
        Text(
            text = "Pengajuan",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 90.dp)
        )

        // Search bar positioned below "Pengajuan" text and center it vertically
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Align the search bar at the bottom and center horizontally
                .padding(horizontal = 50.dp)
                .padding(bottom = 30.dp)
        ) {
            TextField(
                value = "",
                onValueChange = { /* Handle text input here */ },
                placeholder = {
                    Text(
                        text = "Cari",
                        color = Color.Gray, // Adjust color as needed
                        style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Maroon,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,  // Remove the focus indicator line
                    unfocusedIndicatorColor = Color.Transparent // Remove the unfocused indicator line
                )
            )

        }
    }
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
                    "Menunggu konfirmasi" -> kuning
                    "Pengujian ditolak" -> abang
                    "Pengajuan Diterima" -> ijo
                    else -> Color.Black
                }
            )
        }
    }
}

@Composable
fun ListPengajuanScreenAdmin(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedDetail by remember { mutableStateOf(DetailInfo()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with back button and search icon
        Rectangle1217(navController = navController)
        Spacer(modifier = Modifier.height(20.dp))

        // List of submissions
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(2) { index ->
                // Assign the status dynamically based on the index (or data)
                val status = if (index % 2 == 0) "Menunggu konfirmasi" else "Pengujian ditolak"

                ListPengajuanItem(
                    namaSistem = "Nama Sistem ${index + 1}",
                    tanggal = "25/10/2025",
                    jenisSistem = "Sistem Baru",
                    rencanaAnggaran = "Termasuk dalam perencanaan",
                    masalahSistem = "Bug tampilan...",
                    outputHasil = "Hasil yang diinginkan...",
                    status = status,
                    onClick = {
                        selectedDetail = DetailInfo(
                            namaSistem = "Nama Sistem ${index + 1}",
                            tanggal = "25/10/2025",
                            jenisSistem = "Sistem Baru",
                            rencanaAnggaran = "Termasuk dalam perencanaan",
                            masalahSistem = "Bug tampilan beranda",
                            outputHasil = "Tampilan bug clear",
                            status = status // Assign the correct status here
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
            onEditClick = {
                navController.navigate("form_usulan") // Ganti dengan rute yang sesuai
            }
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
    onEditClick: () -> Unit // Fungsi untuk navigasi ke form edit
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
                        imageVector = Icons.Filled.Timer,
                        contentDescription = "Timer Icon",
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
                                    "Hari/Tanggal",
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
                                    .shadow(4.dp, RoundedCornerShape(16.dp)), // Menambahkan shadow pada tombol
                                colors = ButtonDefaults.buttonColors(containerColor = ijo),
                                shape = RoundedCornerShape(16.dp) // Membuat tombol dengan sudut yang membulat
                            ) {
                                Text("Terima", color = Color.White)
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Button(
                                onClick = { showAlasanInput = true }, // Menampilkan input alasan jika Tolak ditekan
                                modifier = Modifier
                                    .width(120.dp) // Menyesuaikan lebar tombol
                                    .shadow(4.dp, RoundedCornerShape(16.dp)), // Menambahkan shadow pada tombol
                                colors = ButtonDefaults.buttonColors(containerColor = abang),
                                shape = RoundedCornerShape(16.dp) // Membuat tombol dengan sudut yang membulat
                            ) {
                                Text("Tolak", color = Color.White)
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
                            .align(Alignment.End) // Menempatkan tombol di ujung kanan
                            .shadow(4.dp, RoundedCornerShape(16.dp)), // Menambahkan shadow pada tombol
                        colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                        shape = RoundedCornerShape(16.dp) // Membuat tombol dengan sudut yang membulat
                    ) {
                        Text("Kirim", color = Color.White)
                    }
                }
            }
        }
    }
}


// Data class for storing the detail information
data class DetailInfo(
    val namaSistem: String = "",
    val tanggal: String = "",
    val jenisSistem: String = "",
    val rencanaAnggaran: String = "",
    val masalahSistem: String = "",
    val outputHasil: String = "",
    val status: String = ""
)




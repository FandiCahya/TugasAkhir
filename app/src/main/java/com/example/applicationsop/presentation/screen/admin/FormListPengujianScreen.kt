package com.example.applicationsop.presentation.screen.admin


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.Api.fetchPengujianList
import com.example.applicationsop.data.DetailInfo
import com.example.applicationsop.models.Pengajuan
import com.example.applicationsop.models.Pengujian
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.presentation.component.HeaderWithSearch
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning

@Composable
fun ListPengujianItem(
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
                    "Pengembangan belum selesai" -> abang
                    "Pengembangan" -> ijo
                    else -> Color.Black
                }
            )
        }
    }
}

@Composable
fun ListPengujianScreenAdmin(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedDetail by remember { mutableStateOf(DetailInfo(id = "")) }
    var pengujianList by remember { mutableStateOf<List<Pengujian>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Fetching the data when the Composable is first launched
        val fetchedPengujianList = fetchPengujianList() // Fetch the data
        pengujianList = fetchedPengujianList // Updating the state
//        println("Pengujian List View :${pengujianList}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HeaderWithSearch(navController = navController, title = "Pengujian")
        Spacer(modifier = Modifier.height(20.dp))

        // List of pengujian items from fetched data
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(pengujianList) { pengujian ->
                val status = when (pengujian.pengembangan.status) {
                    "finished" -> "Pengembangan Selesai"
                    else -> "Pengembangan Belum Selesai"
                }

                ListPengujianItem(
                    namaSistem = pengujian.perangkat_lunak, // System name from the API
                    tanggal = pengujian.tanggal, // Date of testing
                    jenisSistem = pengujian.metode.toString(), // System type (converted to string from Int if needed)
                    rencanaAnggaran = pengujian.pengujian_detail.firstOrNull()?.hasil_diharapkan ?: "Data Tidak Tersedia", // Example of another field
                    masalahSistem = pengujian.pengujian_detail.firstOrNull()?.kasus_uji ?: "Data Tidak Tersedia", // Example of another field
                    outputHasil = pengujian.pengujian_detail.firstOrNull()?.hasil_pengujian ?: "Data Tidak Tersedia", // Example of another field
                    status = status,
                    onClick = {
                        selectedDetail = DetailInfo(
                            id = pengujian.id,
                            namaSistem = pengujian.perangkat_lunak,
                            tanggal = pengujian.tanggal,
                            jenisSistem = pengujian.metode.toString(),
                            rencanaAnggaran = pengujian.pengujian_detail.firstOrNull()?.hasil_diharapkan ?: "Data Tidak Tersedia",
                            masalahSistem = pengujian.pengujian_detail.firstOrNull()?.kasus_uji ?: "Data Tidak Tersedia",
                            outputHasil = pengujian.pengujian_detail.firstOrNull()?.hasil_pengujian ?: "Data Tidak Tersedia",
                            status = status
                        )
                        showPopup = true
                    }
                )
            }
        }
    }

    // Show popup when selectedDetail is not null
    if (showPopup) {
        DetailPopupPengujian(
            onDismiss = { showPopup = false },
            hariTanggal = selectedDetail.tanggal,
            namaSistem = selectedDetail.namaSistem,
            jenisSistem = selectedDetail.jenisSistem,
            rencanaAnggaran = selectedDetail.rencanaAnggaran,
            masalahSistem = selectedDetail.masalahSistem,
            outputHasil = selectedDetail.outputHasil,
            status = selectedDetail.status,
            navController = navController
        )
    }
}




@Composable
fun DetailPopupPengujian(
    onDismiss: () -> Unit,
    hariTanggal: String,
    namaSistem: String,
    jenisSistem: String,
    rencanaAnggaran: String,
    masalahSistem: String,
    outputHasil: String,
    status: String,
    navController: NavController // Menambahkan navController sebagai parameter
) {
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Row for Hari/Tanggal
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Hari/Tanggal", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(": $hariTanggal", color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                // Navigate to the formPengujian screen
                                navController.navigate("formPengujian")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Lanjut ke Form Pengujian", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

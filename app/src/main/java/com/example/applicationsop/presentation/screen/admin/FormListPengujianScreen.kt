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
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengujianList
import com.example.applicationsop.data.DetailPengujian
import com.example.applicationsop.models.Pengujian
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning

@Composable
fun ListPengujianItem(
    perangkat_lunak: String,
    versiPerangkat: String,
    tujuanPengujian: String,
    metodePengujian: String,
    tanggalPengujian: String,
    pelaksanaPengujian: String,
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
                text = perangkat_lunak,
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
    var selectedDetail by remember { mutableStateOf(DetailPengujian(id = "")) }
    var pengujianList by remember { mutableStateOf<List<Pengujian>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Fetching the data when the Composable is first launched
        val fetchedPengujianList = fetchPengujianList() // Fetch the data
        pengujianList = fetchedPengujianList // Updating the state
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

                // Passing the data to ListPengujianItem composable
                ListPengujianItem(
                    perangkat_lunak = pengujian.perangkat_lunak,  // System name from Pengujian object
                    versiPerangkat = pengujian.versi,  // Version from Pengujian object
                    tujuanPengujian = pengujian.tujuan,  // Purpose from Pengujian object
                    metodePengujian = pengujian.metode,  // Testing method from Pengujian object
                    tanggalPengujian = pengujian.tanggal,  // Date of testing
                    pelaksanaPengujian = pengujian.pelaksana.name,  // Executor's name
                    status = pengujian.status,
                    onClick = {
                        // Populate selectedDetail with all required data
                        selectedDetail = DetailPengujian(
                            id = pengujian.id,
                            namaSistem = pengujian.perangkat_lunak,
                            versiPerangkat = pengujian.versi,
                            tujuanPengujian = pengujian.tujuan,
                            metodePengujian = pengujian.metode,
                            tanggalPengujian = pengujian.tanggal,
                            pelaksanaPengujian = pengujian.pelaksana.name,
                            status = pengujian.status
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
            hariTanggal = selectedDetail.tanggalPengujian,
            namaSistem = selectedDetail.namaSistem,
            versiPerangkat = selectedDetail.versiPerangkat,
            tujuanPengujian = selectedDetail.tujuanPengujian,
            metodePengujian = selectedDetail.metodePengujian,
            tanggalPengujian = selectedDetail.tanggalPengujian,
            pelaksanaPengujian = selectedDetail.pelaksanaPengujian,
            navController = navController
        )
    }
}



@Composable
fun DetailPopupPengujian(
    onDismiss: () -> Unit,
    hariTanggal: String,
    namaSistem: String,
    versiPerangkat: String,
    tujuanPengujian: String,
    metodePengujian: String,
    tanggalPengujian: String,
    pelaksanaPengujian: String,
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

                    // Perangkat yang dikembangkan
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Perangkat yang dikembangkan", fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "$namaSistem",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart).padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Versi Perangkat Lunak
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Versi Perangkat Lunak", fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "$versiPerangkat",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart).padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tujuan Pengujian
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Tujuan Pengujian", fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "$tujuanPengujian",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart).padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Metode Pengujian
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Metode Pengujian", fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "$metodePengujian",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart).padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tanggal Pengujian
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Tanggal Pengujian", fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "$tanggalPengujian",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart).padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Pelaksana Pengujian
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Pelaksana Pengujian", fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "$pelaksanaPengujian",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart).padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Button for form
                    Button(
                        onClick = {
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


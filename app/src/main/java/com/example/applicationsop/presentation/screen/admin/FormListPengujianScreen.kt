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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengujianList
import com.example.applicationsop.data.DetailPengujian
import com.example.applicationsop.models.Pengujian
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.presentation.component.popup.DetailPopupPengujian
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

            // Handling status change for testing and finished states
            Text(
                text = when (status) {
                    "testing" -> "Sedang di uji"
                    "finished" -> "Pengujian Selesai"
                    else -> status // Default fallback in case of other statuses
                },
                fontSize = 14.sp,
                color = when (status) {
                    "testing" -> kuning  // Yellow color for testing status
                    "finished" -> ijo  // Green color for finished status
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

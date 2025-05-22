package com.example.applicationsop.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengujianList
import com.example.applicationsop.core.UserUtils
import com.example.applicationsop.data.DetailPengujian
import com.example.applicationsop.models.Catatan
import com.example.applicationsop.models.GetPengujianDetail
import com.example.applicationsop.models.Pengujian
import com.example.applicationsop.models.PengujianDetail
import com.example.applicationsop.models.Persetujuan
import com.example.applicationsop.presentation.component.header.HeaderWithSearch
import com.example.applicationsop.presentation.component.listitem.ListPengujianItem
import com.example.applicationsop.presentation.component.popup.DetailPopupPengujian
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ListPengujianScreenAdmin(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedDetail by remember { mutableStateOf(DetailPengujian(id = "")) }
    var pengujianList by remember { mutableStateOf<List<Pengujian>>(emptyList()) }

    // Informasi User
    val context = LocalContext.current
    val userData = remember { UserUtils.getUserData(context) }
    val userId = userData["userId"]

    LaunchedEffect(Unit) {
        // Fetching the data when the Composable is first launched
        val fetchedPengujianList =
            fetchPengujianList(context, status_persetujuan = "approved") // Fetch the data
        pengujianList = fetchedPengujianList // Updating the state
    }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Parsing the date format
    // Sort pengajuanList by tanggal
    val sortedPengujianList = pengujianList.sortedByDescending { pengujian ->
        try {
            // Try to parse the date string to Date object
            dateFormat.parse(pengujian.tanggal) ?: Date() // Return Date() if parsing fails
        } catch (e: Exception) {
            // If parsing fails, use the current date as fallback
            Date()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HeaderWithSearch(navController = navController, title = "Pengujian")
        Spacer(modifier = Modifier.height(20.dp))

        if (sortedPengujianList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Add Icon with size adjustment
                    Icon(
                        imageVector = Icons.Default.Error, // Ganti dengan ikon yang diinginkan
                        contentDescription = "No Pengajuan",
                        modifier = Modifier
                            .size(70.dp)
                            .padding(bottom = 10.dp), // Sesuaikan ukuran ikon
                        tint = Color.Gray
                    )

                    // Add Text below the icon
                    Text(
                        text = "Tidak Ada Pengujian",
                        color = Color.Gray,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        )
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(sortedPengujianList) { pengujian ->
                    val status = when (pengujian.pengembangan?.status ?: "Tidak Tersedia") {
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
                        pelaksanaPengujian = pengujian.pelaksana?.name
                            ?: "Tidak Tersedia",  // Executor's name
                        status = pengujian.status,
                        persetujuanId = pengujian.persetujuan?.joinToString(", ") {
                            it.id ?: "Tidak Tersedia"
                        } ?: "Tidak Tersedia",

                        onClick = {
                            // Populate selectedDetail with all required data
                            selectedDetail = DetailPengujian(
                                id = pengujian.id,
                                namaSistem = pengujian.perangkat_lunak,
                                versiPerangkat = pengujian.versi,
                                tujuanPengujian = pengujian.tujuan,
                                metodePengujian = pengujian.metode,
                                tanggalPengujian = pengujian.tanggal,
                                pelaksanaPengujian = pengujian.pelaksana?.name ?: "Tidak Tersedia",
                                status = pengujian.status,
                                persetujuanId = pengujian.persetujuan?.joinToString(", ") {
                                    it.id ?: "Tidak Tersedia"
                                } ?: "Tidak Tersedia",
                            )
                            showPopup = true
                        }
                    )
                }
            }
        }
    }

    // Show popup when selectedDetail is not null
    if (showPopup) {
        DetailPopupPengujian(
            onDismiss = { showPopup = false },
            id = selectedDetail.id,
            namaSistem = selectedDetail.namaSistem,
            versiPerangkat = selectedDetail.versiPerangkat,
            tujuanPengujian = selectedDetail.tujuanPengujian,
            metodePengujian = selectedDetail.metodePengujian,
            tanggalPengujian = selectedDetail.tanggalPengujian,
            pelaksanaPengujian = selectedDetail.pelaksanaPengujian,
            status = selectedDetail.status,
            persetujuanId = selectedDetail.persetujuanId,
            navController = navController
        )
    }
}

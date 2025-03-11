package com.example.applicationsop.presentation.screen.admin

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.ui.theme.Putih

fun getUserData(context: Context): Map<String, String?> {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
    val token = sharedPreferences.getString("TOKEN", null)
    val userId = sharedPreferences.getString("USER_ID", null)
    val role = sharedPreferences.getString("ROLE", null)
    val name = sharedPreferences.getString("NAME", null)
    val email = sharedPreferences.getString("EMAIL", null)
    val devisi = sharedPreferences.getString("DEVISI", null)

    return mapOf(
        "token" to token,
        "userId" to userId,
        "role" to role,
        "name" to name,
        "email" to email,
        "devisi" to devisi
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun DetailPengujianAdmin(navController: NavController, idPengembangan: String?, namaSistem: String?) {
    // Informasi Pengujian
    var namaSistem by remember { mutableStateOf("Sistem Monitoring IoT") }
    var versiPerangkat by remember { mutableStateOf("1.0.0") }
    var tujuanPengujian by remember { mutableStateOf("Uji fungsionalitas") }
    var metodePengujian by remember { mutableStateOf("Manual") }
    var tanggalPengujian by remember { mutableStateOf("2025-03-10") }
    var pelaksanaPengujian by remember { mutableStateOf("cCare, UAT") }

    // State untuk input uraian
    var selectedTestType by remember { mutableStateOf("positif") }
    var namaUji by remember { mutableStateOf("Pengujian Sistem A") }
    var kasusUji by remember { mutableStateOf("Kasus 1") }
    var hasilYangDiharapkan by remember { mutableStateOf("Berhasil") }
    var hasilPengujian by remember { mutableStateOf("Berhasil") }
    var keterangan by remember { mutableStateOf("Ok") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(scrollState)
    ) {
        // Header Section
        HeaderForm(title = "Detail Pengujian", navController)

        // Form Fields Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Informasi Pengujian
            Text(text = "Informasi Pengujian", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Divider(color = Color.Gray, thickness = 1.dp)

            // Tabel Informasi Pengujian

            Column(modifier = Modifier.fillMaxWidth()) {
                // Baris kedua (nama perangkat lunak)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Nama  Software", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(namaSistem, color = Color.Black)
                    }
                }
                // Baris kedua (Versi perangkat lunak)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Versi Software", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(versiPerangkat, color = Color.Black)
                    }
                }

                // Baris kedua (Tujuan Pengujian)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Tujuan Pengujian", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(tujuanPengujian, color = Color.Black)
                    }
                }

                // Baris ketiga (Metode Pengujian)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Metode Pengujian", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(metodePengujian, color = Color.Black)
                    }
                }

                // Baris keempat (Tanggal Pengujian)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Tanggal Pengujian", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(tanggalPengujian, color = Color.Black)
                    }
                }

                // Baris keempat (Tanggal Pengujian)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Pelaksana Pengujian", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(pelaksanaPengujian, color = Color.Black)
                    }
                }

                // Informasi Pengujian
                Text(text = "Uraian Pengujian", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(vertical = 10.dp))
                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(bottom = 20.dp))

                // Baris kelima (Jenis Uji)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Jenis Uji", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(selectedTestType, color = Color.Black)
                    }
                }

                // Baris keenam (Nama Uji)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Nama Uji", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(namaUji, color = Color.Black)
                    }
                }

                // Baris ketujuh (Kasus Uji)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Kasus Uji", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(kasusUji, color = Color.Black)
                    }
                }

                // Baris kedelapan (Hasil yang Diharapkan)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Hasil yang Diharapkan", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(hasilYangDiharapkan, color = Color.Black)
                    }
                }

                // Baris kesembilan (Hasil Pengujian)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Hasil Pengujian", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(hasilPengujian, color = Color.Black)
                    }
                }

                // Baris kesepuluh (Keterangan)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text("Keterangan", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        Text(keterangan, color = Color.Black)
                    }
                }
            }
        }
    }
}

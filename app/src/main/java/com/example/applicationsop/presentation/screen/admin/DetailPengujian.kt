//package com.example.applicationsop.presentation.screen.admin
//
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.CheckboxDefaults
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Text
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.applicationsop.ui.theme.Maroon
//import com.example.applicationsop.presentation.component.header.HeaderForm
//import java.util.*
//import com.example.applicationsop.presentation.screen.admin.PengujianData as PengujianData1
//
//@Composable
//fun PengujianDetailScreen(navController: NavController, pengujianId: String?) {
//    // Dummy data for testing - replace with your actual data
//    val pengujian = remember {
//        PengujianData1(
//            perangkat_lunak = "Sistem Pengujian 1", // Dummy data
//            versiPerangkat = "2.1.3", // Dummy data
//            tujuanPengujian = "Memastikan performa sistem dalam kondisi tinggi", // Dummy data
//            metodePengujian = "Stress Testing", // Dummy data
//            tanggalPengujian = "2025-05-15", // Dummy data
//            pelaksanaPengujian = "Admin Pengujian", // Dummy data
//            namaUji = "Uji Performa", // Dummy data
//            kasusUji = "Uji penggunaan CPU dan RAM", // Dummy data
//            hasilDiharapkan = "Sistem dapat berjalan dengan lancar pada beban tinggi", // Dummy data
//            hasilPengujian = "Sistem berjalan stabil pada beban tinggi selama 3 jam", // Dummy data
//            catatan = "Tidak ada masalah signifikan" // Dummy data
//        )
//    }
//
//    // Ensure that the details are scrolled properly
//    val scrollState = rememberScrollState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .verticalScroll(scrollState)
//    ) {
//        // Header Section
//        HeaderForm(title = "Detail Pengujian", navController)
//
//        // Informasi Pengujian
//        Text(
//            text = "Informasi Pengujian",
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//        Divider(color = Color.Gray, thickness = 1.dp)
//
//        // Display pengujian information with dummy data
//        Text("Perangkat yang diuji: ${pengujian.perangkat_lunak}")
//        Text("Versi: ${pengujian.versiPerangkat}")
//        Text("Tujuan: ${pengujian.tujuanPengujian}")
//        Text("Metode: ${pengujian.metodePengujian}")
//        Text("Tanggal Pengujian: ${pengujian.tanggalPengujian}")
//        Text("Pelaksana: ${pengujian.pelaksanaPengujian}")
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Uraian Pengujian
//        Text(
//            text = "Uraian Pengujian",
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//        Divider(color = Color.Gray, thickness = 1.dp)
//
//        Text("Nama Uji: ${pengujian.namaUji}")
//        Text("Kasus Uji: ${pengujian.kasusUji}")
//        Text("Hasil Yang Diharapkan: ${pengujian.hasilDiharapkan}")
//        Text("Hasil Pengujian: ${pengujian.hasilPengujian}")
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display Catatan if available
//        pengujian.catatan?.let {
//            Text(
//                text = "Catatan Pengujian",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black
//            )
//            Divider(modifier = Modifier.padding(top = 8.dp))
//
//            Text(
//                text = it,
//                fontSize = 16.sp,
//                color = Color.Black
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Approval Section (Checkboxes)
//        Text(
//            text = "Approval",
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//        Divider(modifier = Modifier.padding(bottom = 8.dp))
//
//        // Checkbox Options for approval roles
//        val roles = listOf("Admin", "Pemohon", "QMR", "Kacab")
//        val checkedStates = remember { mutableStateOf(mapOf<String, Boolean>()) }
//
//        Column(modifier = Modifier.fillMaxWidth()) {
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(18.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                roles.take(2).forEach { role ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Checkbox(
//                            checked = checkedStates.value[role] == true,
//                            onCheckedChange = { isChecked ->
//                                checkedStates.value = checkedStates.value + (role to isChecked)
//                            },
//                            colors = CheckboxDefaults.colors(
//                                checkedColor = Maroon,
//                                uncheckedColor = Color.LightGray,
//                                checkmarkColor = Color.White
//                            )
//                        )
//                        Text(text = role, fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp), color = Maroon)
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(18.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                roles.drop(2).forEach { role ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Checkbox(
//                            checked = checkedStates.value[role] == true,
//                            onCheckedChange = { isChecked ->
//                                checkedStates.value = checkedStates.value + (role to isChecked)
//                            },
//                            colors = CheckboxDefaults.colors(
//                                checkedColor = Maroon,
//                                uncheckedColor = Color.LightGray,
//                                checkmarkColor = Color.White
//                            )
//                        )
//                        Text(text = role, fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp), color = Maroon)
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun PengujianData(
//    perangkat_lunak: String,
//    versiPerangkat: String,
//    tujuanPengujian: String,
//    metodePengujian: String,
//    tanggalPengujian: String,
//    pelaksanaPengujian: String,
//    namaUji: String,
//    kasusUji: String,
//    hasilDiharapkan: String,
//    hasilPengujian: String,
//    catatan: String
//) {
//
//}

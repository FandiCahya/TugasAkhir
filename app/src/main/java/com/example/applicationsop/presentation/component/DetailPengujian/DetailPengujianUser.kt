package com.example.applicationsop.presentation.component.DetailPengujian

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengujianList
import com.example.applicationsop.models.Pengujian
import com.example.applicationsop.presentation.component.DetailPengembangan.DetailItem
import com.example.applicationsop.ui.theme.BiruMuda
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.biru
import com.example.applicationsop.ui.theme.birutua
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning

@Composable
fun DetailPengujianScreen(
    navController: NavController,
    id: String,
    userId: String?
) {
    val context = LocalContext.current
    var pengujian by remember { mutableStateOf<Pengujian?>(null) }

    LaunchedEffect(id) {
        val result = fetchPengujianList(context, user_id = userId)
        pengujian = result.find { it.pengembangan?.pengajuan?.id == id }
    }

    if (pengujian == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }


    val persetujuanId = pengujian?.persetujuan?.firstOrNull()?.id
    val pengembangan = pengujian!!.pengembangan
    val pengajuan = pengembangan!!.pengajuan
    val pengajuanStatusColor = when (pengajuan?.status) {
        "approval" -> BiruMuda
        "testing" -> ijo
        "finished" -> abang
        else -> Color.LightGray
    }
    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        navController.navigate(
                            "detail_approval_screen/persetujuanId=$persetujuanId"
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = biru)
                ) {
                    Text("Persetujuan", color = Color.White)
                }
                Button(
                    onClick = {
                        navController.navigate("DetailPengujianPemohon?id=${pengujian?.id}&namaSistem=${pengajuan?.nama_sistem}")
                        {
                            popUpTo("DetailPengujianPemohon") { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ijo)
                ) {
                    Text("Setujui", color = Color.White)
                }
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
                Text(
                    text = pengajuan?.status?.replaceFirstChar { c -> c.uppercaseChar() } ?: "-",
                    color = pengajuanStatusColor,
                    modifier = Modifier
                        .background(pengajuanStatusColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp
                )

            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = pengajuan?.nama_sistem ?: "Nama Sistem",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Diajukan oleh: ${pengajuan?.user?.name ?: "-"}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Diuji oleh: ${pengujian?.pelaksana?.name ?: "-"}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Informasi Pengajuan
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Informasi Pengajuan", style = MaterialTheme.typography.titleMedium,color = birutua )
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItem("Masalah", pengajuan?.masalah)
                    DetailItem("Output", pengajuan?.output)
                    DetailItem("Jenis Usulan", pengajuan?.jenis?.replace("_", " ")?.replaceFirstChar { it.uppercaseChar() })
                    DetailItem("Rencana Anggaran", pengajuan?.rencana_anggaran?.replace("_", " ")?.replaceFirstChar { it.uppercaseChar() })
                    DetailItem("Tanggal Pengajuan", pengajuan?.tgl)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Informasi Pengembangan
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Informasi Pengembangan", style = MaterialTheme.typography.titleMedium, color = birutua)
                    Spacer(modifier = Modifier.height(8.dp))

                    DetailItem("Tanggal Mulai", pengembangan?.tanggal_mulai)
                    DetailItem("Tanggal Selesai", pengembangan?.tanggal_selesai)
                    DetailItem("Keterangan", pengembangan?.keterangan)

                    Text("Tahap Pengerjaan", fontSize = 12.sp, color = Color.Gray)

                    val tahapList = listOf("Analisis", "Desain UI UX", "Pengerjaan", "Penyelesaian", "Testing")
                    val selesai = (pengembangan?.persentase ?: 0) >= 100
                    val tahapSelesai = pengembangan?.tahap
                        ?.lowercase()
                        ?.split(",")
                        ?.map { it.trim() }
                        ?.toSet() ?: emptySet()

                    tahapList.forEach { tahap ->
                        val isDone = selesai || tahap.lowercase() in tahapSelesai
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (isDone) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (isDone) ijo else Color.Red
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(tahap, fontSize = 14.sp, color = Color.Black)
                        }
                    }

                    val percent = pengembangan?.persentase ?: 0
                    Text("Persentase", fontSize = 12.sp, color = Color.Gray)
                    LinearProgressIndicator(
                        progress = percent / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = ijo,
                        trackColor = Color.LightGray
                    )
                    Text(
                        "$percent%",
                        modifier = Modifier.align(Alignment.End),
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Informasi Pengujian
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Informasi Pengujian", style = MaterialTheme.typography.titleMedium, color = birutua)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItem("Versi", pengujian?.versi)
                    DetailItem("Tanggal Uji", pengujian?.tanggal)
                    DetailItem("Tujuan", pengujian?.tujuan)
                    DetailItem("Metode", pengujian?.metode)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Detail Pengujian
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Detail Pengujian", style = MaterialTheme.typography.titleMedium, color = birutua)
                    Spacer(modifier = Modifier.height(8.dp))

                    pengujian?.pengujian_detail?.takeIf { it.isNotEmpty() }?.forEachIndexed { index, detail ->
                        Text(
                            text = "Pengujian ${index + 1}",
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                        DetailItem("Nama Uji", detail.nama_uji)
                        DetailItem("Kasus Uji", detail.kasus_uji)
                        DetailItem("Hasil Diharapkan", detail.hasil_diharapkan)
                        DetailItem("Hasil Pengujian", detail.hasil_pengujian)
                        DetailItem("Kategori", detail.kategori.replace("_", " ").replaceFirstChar { it.uppercaseChar() })
                        DetailItem("Status", detail.status)
                        Spacer(modifier = Modifier.height(12.dp))
                    } ?: Text("Belum ada data pengujian.", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(50.dp)) // Spacer untuk memberi jarak dari tombol
        }
    }
}



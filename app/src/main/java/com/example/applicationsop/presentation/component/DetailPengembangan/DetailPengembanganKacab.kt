package com.example.applicationsop.presentation.component.DetailPengembangan

import android.annotation.SuppressLint
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
import com.example.applicationsop.Api.fetchPengembanganSortList
import com.example.applicationsop.models.Pengembangan
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.birutua
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning

@SuppressLint("SimpleDateFormat")
@Composable
fun DetailPengembanganScreenKacab(
    navController: NavController,
    id: String,
) {
    val context = LocalContext.current
    var pengembangan by remember { mutableStateOf<Pengembangan?>(null) }

    LaunchedEffect(id) {
            val data = fetchPengembanganSortList(context)
            pengembangan = data.find { it.pengajuan.id == id }
    }

    if (pengembangan == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val dev = pengembangan!!.pengajuan
    val statusColor = when (pengembangan?.status) {
        "developed" -> kuning
        "testing" -> ijo
        "finished" -> abang
        else -> Color.LightGray
    }

    Surface(
        modifier = Modifier.fillMaxSize().background(Color.White),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
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
                    text = pengembangan!!.status.replaceFirstChar { it.uppercaseChar() },
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    color = statusColor,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nama Sistem
            Text(
                text = dev?.nama_sistem ?: "Nama Sistem",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Diajukan oleh: ${dev?.user?.name ?: "-"}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card Informasi Pengajuan
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informasi Usulan",
                        style = MaterialTheme.typography.titleMedium,
                        color = birutua
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItem("Masalah", dev?.masalah)
                    DetailItem("Output", dev?.output)
                    DetailItem("Jenis Usulan", dev?.jenis?.replace("_", " ")?.replaceFirstChar { it.uppercaseChar() })
                    DetailItem("Rencana Anggaran", dev?.rencana_anggaran?.replace("_", " ")?.replaceFirstChar { it.uppercaseChar() })
                    DetailItem("Tanggal Pengajuan", dev?.tgl)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card Informasi Pengembangan
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informasi Pengembangan",
                        style = MaterialTheme.typography.titleMedium,
                        color = birutua
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    DetailItem("Tanggal Mulai", pengembangan?.tanggal_mulai)
                    DetailItem("Tanggal Selesai", pengembangan?.tanggal_selesai)
                    DetailItem("Keterangan", pengembangan?.keterangan)

                    // Tahap Pengerjaan
                    // Tahap Pengerjaan sebagai checklist
                    Text(text = "Tahap Pengerjaan", fontSize = 12.sp, color = Color.Gray)

                    val tahapList = listOf("Analisis", "Desain UI UX", "Pengerjaan", "Penyelesaian", "Testing")
                    val selesai = (pengembangan?.persentase ?: 0) >= 100
                    val tahapSelesai = pengembangan?.tahap
                        ?.lowercase()
                        ?.split(",")
                        ?.map { it.trim() }
                        ?.toSet() ?: emptySet()

                    tahapList.forEach { tahap ->
                        val isDone = selesai || tahap.lowercase() in tahapSelesai
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(
                                imageVector = if (isDone) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (isDone) ijo else Color.Red
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = tahap, fontSize = 14.sp, color = Color.Black)
                        }
                    }

                    // Progress Bar
                    val percent = pengembangan?.persentase ?: 0
                    Text(text = "Persentase", fontSize = 12.sp, color = Color.Gray)
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
                        text = "$percent%",
                        modifier = Modifier.align(Alignment.End),
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


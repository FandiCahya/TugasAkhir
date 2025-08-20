package com.example.applicationsop.presentation.component.DetailPengembangan

import android.annotation.SuppressLint
import android.widget.Toast
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
import com.example.applicationsop.Api.updatePengembangan
import com.example.applicationsop.models.Pengembangan
import com.example.applicationsop.models.UpdatePengembangan
import com.example.applicationsop.ui.theme.BiruMuda
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.birutua
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch

@SuppressLint("SimpleDateFormat")
@Composable
fun DetailPengembanganAdmin(
    navController: NavController,
    id: String,
) {
    val context = LocalContext.current
    var pengembangan by remember { mutableStateOf<Pengembangan?>(null) }
    val coroutineScope = rememberCoroutineScope()


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
    val tahapList = listOf("Analisis", "Desain UI UX", "Pengerjaan", "Penyelesaian", "Testing")

    // State: tahap dicentang
        val selectedStages = remember {
            mutableStateListOf<String>().apply {
                val tahapSelesai = pengembangan?.tahap
                    ?.lowercase()
                    ?.split(",")
                    ?.map { it.trim() }
                    ?.filter { it.isNotBlank() } ?: emptyList()
                addAll(tahapSelesai)
            }
        }

    // Hitung persentase otomatis berdasarkan checklist
    val progressPercentageState = remember(selectedStages) {
            (selectedStages.size * 100) / tahapList.size
    }

    var selectedStagesState by remember { mutableStateOf(pengembangan?.tahap) }
    val dev = pengembangan!!.pengajuan
    val statusColor = when (pengembangan?.status) {
        "developed" -> kuning
        "finished" -> ijo
        "testing" -> birutua
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
                    Text(text = "Tahap", fontSize = 12.sp, color = Color.Gray)

                    if (pengembangan?.status == "developed") {
                        Text(text = "Checklist Tahap Pengembangan", fontSize = 9.sp, color = Color.Gray)

                        tahapList.forEach { tahap ->
                            val isChecked = selectedStages.contains(tahap.lowercase())
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            selectedStages.add(tahap.lowercase())
                                        } else {
                                            selectedStages.remove(tahap.lowercase())
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = ijo)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = tahap, fontSize = 14.sp, color = Color.Black)

                            }
                        }

                        // Progress Bar Berdasarkan Checkbox
                        val percent = (selectedStages.size * 100) / tahapList.size
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
                    } else {
                        // Tampilkan hanya checklist biasa jika bukan developed
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

                        // Progress Bar berdasarkan dari data API
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (pengembangan?.status == "developed") {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val tahapFinal = selectedStages.joinToString(", ")
                                val safePercentage = progressPercentageState.coerceIn(0, 100)
                                val newStatus = if (safePercentage == 100) "finished" else "developed"

                                val updatedPengembangan = UpdatePengembangan(
                                    tahap = tahapFinal,
                                    persentase = safePercentage,
                                    status = newStatus
                                )
                                val response = updatePengembangan(context, pengembangan!!.id, updatedPengembangan)

                                if (response.status.value in 200..299) {
                                    Toast.makeText(context, "Update berhasil!", Toast.LENGTH_SHORT).show()
                                    pengembangan = pengembangan?.copy(
                                        tahap = tahapFinal,
                                        persentase = safePercentage,
                                        status = newStatus
                                    )
                                } else {
                                    val errorMessage = response.bodyAsText()
                                    Toast.makeText(context, "Gagal update: $errorMessage", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.7f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6EF6))
                ) {
                    Text("Update Pengembangan", color = Color.White)
                }
            }

            if (pengembangan?.status == "finished") {
                Button(
                    onClick = {
                        navController.navigate(
                            "formPengujian?id=$id&taskName=${dev?.nama_sistem}"
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.7f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6EF6))
                ) {
                    Text("Tahap Pengujian", color = Color.White)
                }
            }
        }
    }
}
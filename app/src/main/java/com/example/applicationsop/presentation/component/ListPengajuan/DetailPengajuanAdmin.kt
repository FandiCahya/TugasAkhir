package com.example.applicationsop.presentation.component.ListPengajuan
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengajuanHome
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.Api.updatePengajuan
import com.example.applicationsop.models.Pengajuan
import com.example.applicationsop.models.PengajuanRequest
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale

@SuppressLint("SimpleDateFormat")
@Composable
fun DetailUsulanAdmin(
    navController: NavController,
    id: String,
    userId: String?,
    alasan: String? = null
) {
    val context = LocalContext.current
    var usulan by remember { mutableStateOf<Pengajuan?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var inputAlasan by remember { mutableStateOf(alasan.orEmpty()) }
    var showAlasanInput by remember { mutableStateOf(false) }
    var pengajuanList by remember { mutableStateOf<List<Pengajuan>>(emptyList()) }

    LaunchedEffect(id) {
            val data = fetchPengajuanHome(context)
//            println("User ID Detail Pengajuan: $userId")
//            println("List ID pengajuan: ${data.map { it.id }}")
//            println("ID pengajuan yang dicari: $id")
            usulan = data.find { it.id == id }
    }

    if (usulan == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val statusColor = when (usulan?.status) {
        "pending" -> kuning
        "accepted" -> ijo
        "rejected" -> abang
        else -> Color.LightGray
    }

    // Gunakan Surface agar background putih menutupi seluruh layar
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
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
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back",tint = Color.Black)
                }
                Text(
                    text = usulan?.status?.replaceFirstChar { it.uppercaseChar() } ?: "-",
                    modifier = Modifier
                        .background(statusColor.copy(0.2f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    color = statusColor,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = usulan?.nama_sistem ?: "Nama Sistem",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Diajukan oleh: ${usulan?.user?.name ?: "-"}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailItem("Masalah", usulan?.masalah)
                    DetailItem("Output", usulan?.output)
                    DetailItem(
                        "Jenis Usulan",
                        usulan?.jenis?.replace("_", " ")?.replaceFirstChar { it.uppercaseChar() }
                    )
                    DetailItem(
                        "Rencana Anggaran",
                        usulan?.rencana_anggaran?.replace("_", " ")?.replaceFirstChar { it.uppercaseChar() }
                    )
                    DetailItem("Tanggal Pengajuan", usulan?.tgl)
                    if (usulan?.status == "rejected" && !usulan?.alasan_penolakan.isNullOrBlank()) {
                        DetailItem("Alasan Penolakan", usulan?.alasan_penolakan)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (usulan?.status == "pending") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val pengajuanRequest = PengajuanRequest(status = "accepted")

                            coroutineScope.launch {
                                try {
                                    val response = updatePengajuan(context, id, pengajuanRequest)
                                    if (response.status.value in 200..299) {
                                        // Refresh usulan dari API setelah update berhasil
                                        val updatedData = fetchPengajuanHome(context)
                                        usulan = updatedData.find { it.id == id }
                                    } else {
                                        println("Failed to update status")
                                    }
                                } catch (e: Exception) {
                                    println("Error: ${e.message}")
                                }
                            }
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = ijo),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Terima", color = Color.White)
                    }


                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            // Show the reason input for rejection
                            showAlasanInput = true
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = abang),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Tolak", color = Color.White)
                    }
                }

                // Tampilkan input alasan jika tombol Tolak ditekan
                if (showAlasanInput) {
                    // Input alasan
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            "Alasan Penolakan",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = inputAlasan,
                            onValueChange = { inputAlasan = it },
                            placeholder = { Text("Masukkan alasan") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                if (inputAlasan.isNotEmpty()) {
                                    val pengajuanRequest = PengajuanRequest(
                                        status = "rejected",
                                        alasan_penolakan = inputAlasan
                                    )

                                    coroutineScope.launch {
                                        try {
                                            val response = updatePengajuan(context, id, pengajuanRequest)
                                            if (response.status.value in 200..299) {
                                                // Refresh usulan dari API setelah update berhasil
                                                val updatedData = fetchPengajuanHome(context)
                                                usulan = updatedData.find { it.id == id }
                                                showAlasanInput = false
                                            } else {
                                                println("Failed to update status")
                                            }
                                        } catch (e: Exception) {
                                            println("Error: ${e.message}")
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(containerColor = abang),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Kirim Alasan", color = Color.White)
                        }
                    }
                }
            }

            if (usulan?.status == "accepted") {
                Button(
                    onClick = {
                        navController.navigate(
                            "addSchedule?id=$id&namaSistem=${usulan!!.nama_sistem}"
                        )
                              },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.7f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6EF6))
                ) {
                    Text("Tahap Pengembangan", color = Color.White)
                }
            }
        }
    }
}
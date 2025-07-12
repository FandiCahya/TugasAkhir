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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.models.Pengajuan
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale

@SuppressLint("SimpleDateFormat")
@Composable
fun DetailUsulanScreen(
    navController: NavController,
    id: String,
    role: String?,
    devisi: String?
) {
    val context = LocalContext.current
    var usulan by remember { mutableStateOf<Pengajuan?>(null) }

    LaunchedEffect(id) {
        if (role != null && devisi != null) {
            val data = fetchPengajuanList(context, status = null, role, devisi)
            usulan = data.find { it.id == id }
        }
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

            if (usulan?.status == "rejected") {
                Button(
                    onClick = {
                        navController.navigate("form_edit_usulan?id=${usulan?.id}")
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.7f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6EF6))
                ) {
                    Text("Edit Usulan", color = Color.White)
                }
            }
        }
    }
}


@Composable
fun DetailItem(title: String, value: String?) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, fontSize = 12.sp, color = Color.Gray)
        Text(text = value ?: "-", fontSize = 14.sp, color = Color.Black)
    }
}


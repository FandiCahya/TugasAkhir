package com.example.applicationsop.presentation.component.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.models.PersetujuanPengujianDetail
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ListLaporanItem(
    tgl: String,
    nama_sistem: String,
    jenis: String,
    rencana_anggaran: String,
    masalah: String,
    output: String,
    tanggal_mulai: String?,
    tanggal_selesai: String?,
    tahap: String?,
    keterangan: String?,
    perangkat_lunak: String?,
    versiPerangkat: String?,
    tujuanPengujian: String?,
    metodePengujian: String?,
    detailPersetujuan: List<PersetujuanPengujianDetail>,
    status: String,
    onClick: () -> Unit
)
 {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val parsedDate = try {
        dateFormat.parse(tgl)
    } catch (e: Exception) {
        null
    }

    val formattedDate = if (parsedDate != null) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsedDate)
    } else {
        "Invalid Date"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(Color(0xFFF6F6F6))
            .padding(16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF6F6F6), shape = CircleShape)
                .border(2.dp, Color.Black, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Description,
                contentDescription = "Laporan Icon",
                modifier = Modifier.fillMaxSize().padding(5.dp),
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nama_sistem,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Divider(color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = status,
                    fontSize = 14.sp,
                    color = when (status.lowercase(Locale.ROOT)) {
                        "pending" -> kuning
                        "rejected" -> abang
                        "finished" -> ijo
                        else -> Color.Black
                    }
                )
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

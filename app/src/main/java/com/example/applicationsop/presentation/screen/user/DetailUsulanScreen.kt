package com.example.applicationsop.presentation.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih

@Composable
fun DetailUsulanPopup(
    navController: NavController,
    usulan: UsulanData,
    onDismissRequest: () -> Unit,
    onEditRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Putih)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (usulan.status == "Menunggu konfirmasi") Icons.Default.Info else Icons.Default.Error,
                        contentDescription = null,
                        tint = if (usulan.status == "Menunggu konfirmasi") Color.Blue else Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Detail Usulan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Maroon
                    )
                }

                // Detail Fields
                DetailField(label = "Hari/Tanggal", value = usulan.date)
                DetailField(label = "Nama Sistem", value = usulan.systemName)
                DetailField(label = "Jenis", value = usulan.type)
                DetailField(label = "Rencana Anggaran", value = usulan.budgetPlan)
                DetailField(label = "Masalah pada sistem yang ada", value = usulan.issue)
                DetailField(label = "Output/hasil yang diharapkan", value = usulan.expectedOutput)
                DetailField(label = "Status", value = usulan.status)

                // Alasan jika ditolak
                if (usulan.status == "Ditolak") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Alasan: ${usulan.reason}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onEditRequest() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(text = "Edit", color = Putih, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailField(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Maroon
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
    }
}

data class UsulanData(
    val date: String,
    val systemName: String,
    val type: String,
    val budgetPlan: String,
    val issue: String,
    val expectedOutput: String,
    val status: String,
    val reason: String? = null // Alasan hanya ditampilkan jika status "Ditolak"
)

// Contoh penggunaan:
@Composable
fun ExampleUsage(navController: NavController) {
    var showPopup by remember { mutableStateOf(true) }
    val exampleUsulan = UsulanData(
        date = "29/02/2025",
        systemName = "Example System",
        type = "Sistem baru",
        budgetPlan = "Termasuk dalam perencanaan",
        issue = "Bug tampilan...",
        expectedOutput = "Bug tampilan...",
        status = "Ditolak",
        reason = "Output kurang jelas"
    )

    if (showPopup) {
        DetailUsulanPopup(
            navController = navController,
            usulan = exampleUsulan,
            onDismissRequest = { showPopup = false },
            onEditRequest = { /* Arahkan ke form usulan */ }
        )
    }
}

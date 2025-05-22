package com.example.applicationsop.presentation.screen.admin

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.applicationsop.Api.fetchApprovalList
import com.example.applicationsop.models.Approval
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.ui.theme.Maroon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext

@SuppressLint("UnrememberedMutableState")
@Composable
fun DetailApproval(navController: NavController, persetujuanId: String?) {
    var approvals by remember { mutableStateOf<List<Approval>>(emptyList()) }
    val context = LocalContext.current

    // Fetch data dari API
    LaunchedEffect(persetujuanId) {
        approvals = fetchApprovalList(context, persetujuanId)
        println("Laporan List: ${approvals}")
    }

    val scrollState = rememberScrollState()

    val roleOrder = listOf("pemohon", "admin", "qmr", "kepalacabang")

    // Mengurutkan approvals berdasarkan role sesuai dengan urutan yang diinginkan
    val sortedApprovals = approvals.sortedBy { approval ->
        roleOrder.indexOf(approval.user.role?.lowercase() ?: "") // Mengurutkan berdasarkan posisi role di roleOrder
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Header Section
        HeaderForm(title = "Detail Approval", navController)

        // Signature Section for each role
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            sortedApprovals.forEachIndexed { index, approval ->
                val signatureUrl = approval.getSignatureUrl() ?: ""
                val role = approval.user.role ?: ""
                val catatan = approval.catatan ?: "Tidak ada catatan"

                Log.d("ApprovalDebug", "Signature URL for $role: $signatureUrl")

                ApprovalSection(role, signatureUrl, catatan)

                // Garis pemisah antar approval
                if (index < sortedApprovals.size - 1) {
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
fun ApprovalSection(role: String, signatureUrl: String?, catatan: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // Menambahkan padding keseluruhan untuk menjaga jarak antar elemen
        horizontalAlignment = Alignment.Start
    ) {
        // Mengubah nama role sesuai dengan kebutuhan
        val roleDisplay = when (role.lowercase()) {
            "user" -> "Pemohon"
            "qmr" -> "QMR"
            "kepalacabang" -> "Kepala Cabang"
            "admin" -> "Admin"
            else -> role // Menggunakan role yang diberikan jika tidak sesuai dengan yang diharapkan
        }

        // Judul
        Text(
            text = "$roleDisplay Approval" +
                    "",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Maroon
        )

        Spacer(modifier = Modifier.height(8.dp)) // Memberikan jarak antara judul dan gambar

        // Menampilkan tanda tangan dengan loading indicator
        if (!signatureUrl.isNullOrEmpty()) {
            var isLoading by remember { mutableStateOf(true) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp) // Sesuaikan ukuran gambar agar lebih proporsional
                    .clip(RoundedCornerShape(30.dp)), // Memberikan efek rounded corners pada gambar
//                    .background(Color.LightGray), // Menambahkan latar belakang pada Box
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = signatureUrl,
                    contentDescription = "Signature of $role",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    onSuccess = { isLoading = false },
                    onError = { isLoading = false }
                )

                // Loading Indicator saat gambar dimuat
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp), // Ukuran loading indicator
                        color = Maroon
                    )
                }
            }
        } else {
            // Jika tidak ada tanda tangan, tampilkan ikon error besar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp), // Ukuran ikon error
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "No Signature",
                    modifier = Modifier.size(60.dp), // Perbesar ikon error untuk penekanan
                    tint = Color.Gray
                )
                Text("Tidak ada tanda tangan", color = Color.Gray, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp)) // Memberikan jarak antara gambar dan catatan

        // Menampilkan catatan
        Text(
            text = "Catatan: $catatan",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 8.dp) // Memberikan sedikit padding horizontal pada catatan
        )
    }
}


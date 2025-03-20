package com.example.applicationsop.presentation.screen.admin

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.applicationsop.Api.fetchApprovalList
import com.example.applicationsop.R
import com.example.applicationsop.models.Approval
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.ui.theme.Maroon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailApproval(navController: NavController, persetujuanId: String?) {
    var approvals by remember { mutableStateOf<List<Approval>>(emptyList()) }

    // Fetch data dari API
    LaunchedEffect(persetujuanId) {
        approvals = fetchApprovalList(persetujuanId)
    }

    Log.d("ApprovalDebug", "List Approval = $approvals")

    val scrollState = rememberScrollState()

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            approvals.forEach { approval ->
                val signatureUrl = approval.getSignatureUrl()?: ""
                val role = approval.user.role ?: ""
                val catatan = approval.catatan ?: "Tidak ada catatan"

                Log.d("ApprovalDebug", "Signature URL for $role: $signatureUrl")

                ApprovalSection(role, signatureUrl, catatan)
            }
        }
    }
}

@Composable
fun ApprovalSection(role: String, signatureUrl: String?, catatan: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Judul
        Text(
            text = "$role Signature",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Maroon
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Menampilkan tanda tangan dengan loading indicator
        if (!signatureUrl.isNullOrEmpty()) {
            var isLoading by remember { mutableStateOf(true) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = signatureUrl,
                    contentDescription = "Signature of $role",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Fit,
                    onSuccess = { isLoading = false },
                    onError = { isLoading = false }
                )

                // Loading Indicator saat gambar dimuat
                if (isLoading) {
                    CircularProgressIndicator(color = Maroon)
                }
            }
        } else {
            // Jika tidak ada tanda tangan, tampilkan ikon error besar

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "No Signature",
                    modifier = Modifier.size(60.dp), // Perbesar ikon error
                    tint = Color.Gray
                )
                Text("Tidak ada tanda tangan", color = Color.Gray, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Menampilkan catatan
        Text(
            text = "Catatan: $catatan",
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

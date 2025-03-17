package com.example.applicationsop.presentation.screen.pemohon

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.presentation.component.signaturepad.PathState
import com.example.applicationsop.presentation.component.signaturepad.SignatureDialog
import com.example.applicationsop.ui.theme.Maroon

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailApprovalUser(navController: NavController, idPengujian: String?) {
    var adminSignature by remember { mutableStateOf<Bitmap?>(null) }
    var pemohonSignature by remember { mutableStateOf<Bitmap?>(null) }
    var qmrSignature by remember { mutableStateOf<Bitmap?>(null) }
    var kacabSignature by remember { mutableStateOf<Bitmap?>(null) }

    // State for controlling the signature dialog
    var isDialogOpen by remember { mutableStateOf(false) }
    var currentRole by remember { mutableStateOf<String?>(null) } // Track which role's signature is being captured

    val capturingViewBound = remember { mutableStateOf<android.graphics.Rect?>(null) }
    val drawColor = remember { mutableStateOf(Color.Black) }
    val drawBrush = remember { mutableStateOf(5f) }
    val usedColors = remember { mutableStateOf(mutableSetOf<Color>()) }
    val paths = remember { mutableStateOf(mutableListOf<PathState>()) }
    val image = remember { mutableStateOf<Bitmap?>(null) }

    paths.value.add(PathState(Path(), drawColor.value, drawBrush.value))

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
            // Admin Signature
            SignatureSection("Admin Signature", adminSignature, "Admin") {
                currentRole = "Admin"
                isDialogOpen = true
            }

            // Pemohon Signature (only display)
            SignatureSection("Pemohon Signature", pemohonSignature, "Pemohon", openSignatureDialog = {})

            // QMR Signature (only display)
            SignatureSection("QMR Signature", qmrSignature, "QMR", openSignatureDialog = {})

            // Kacab Signature (only display)
            SignatureSection("Kacab Signature", kacabSignature, "Kacab", openSignatureDialog = {})
        }

        // Floating Action Button (FAB) to add signature
        FloatingActionButton(
            onClick = {
                // When FAB is clicked, set the dialog open and role for signature
                currentRole = "Admin" // Set to Admin or whichever role needs the signature
                isDialogOpen = true
            },
            modifier = Modifier
                .padding(start = 280.dp)
                .zIndex(1f)
                .padding(16.dp),
            containerColor = Maroon
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }

    // Signature Dialog for Drawing Signature
    if (isDialogOpen) {
        SignatureDialog(
            isDialogOpen = mutableStateOf(isDialogOpen),
            capturingViewBound = capturingViewBound,
            drawColor = drawColor,
            drawBrush = drawBrush,
            usedColors = usedColors,
            paths = paths,
            image = image
        )
    }

    // Display the signature image once captured for the respective role
    when (currentRole) {
        "Admin" -> {
            adminSignature = image.value
            adminSignature?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Admin Signature",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
        "Pemohon" -> {
            pemohonSignature = image.value
            pemohonSignature?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Pemohon Signature",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
        "QMR" -> {
            qmrSignature = image.value
            qmrSignature?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "QMR Signature",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
        "Kacab" -> {
            kacabSignature = image.value
            kacabSignature?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Kacab Signature",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun SignatureSection(title: String, signature: Bitmap?, role: String, openSignatureDialog: () -> Unit) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Maroon
    )
    Spacer(modifier = Modifier.height(8.dp))

    // Display the signature if available
    if (signature != null) {
        Image(
            bitmap = signature.asImageBitmap(),
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    } else {
        Text("No signature available", color = Color.Gray)
    }
}



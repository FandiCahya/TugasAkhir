package com.example.applicationsop.presentation.screen.pemohon.form

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.postPengajuan
import com.example.applicationsop.models.PengajuanRequest
import com.example.applicationsop.presentation.component.ActionButton
import com.example.applicationsop.presentation.component.DatePickerField
import com.example.applicationsop.presentation.component.DropdownField
import com.example.applicationsop.presentation.component.FormField
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.presentation.component.signaturepad.PathState
import com.example.applicationsop.presentation.component.signaturepad.SignatureDialog
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import kotlinx.coroutines.launch
import java.io.File


fun convertDateToApiFormat(date: String): String {
    // Format tanggal yang diterima dalam format dd/MM/yyyy menjadi yyyy-MM-dd
    val parts = date.split("/")
    return if (parts.size == 3) {
        val day = parts[0].padStart(2, '0')
        val month = parts[1].padStart(2, '0')
        val year = parts[2]
        "$year-$month-$day"
    } else {
        ""  // Return empty string if date format is not valid
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormUsulanScreen(navController: NavController, userId: String?) {
    Log.d("FormUsulanScreen", "userId: $userId")

    // State untuk menyimpan inputan form
    var namaSistem by remember { mutableStateOf("") }
    var jenisSistem by remember { mutableStateOf("") }
    var rencanaAnggaran by remember { mutableStateOf("") }
    var masalahSistem by remember { mutableStateOf("") }
    var outputSistem by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }

    // To show success or error messages
    var isLoading by remember { mutableStateOf(false) }

    // Signature Pad
    val paths = remember { mutableStateOf(mutableListOf<PathState>()) }
    val capturingViewBounds = remember { mutableStateOf<Rect?>(null) }
    val image = remember { mutableStateOf<Bitmap?>(null) }
    val isDialogOpen = remember { mutableStateOf(false) }
    val drawColor = remember { mutableStateOf(Color.Black) }
    val drawBrush = remember { mutableStateOf(5f) }
    val usedColors = remember { mutableStateOf(mutableSetOf(Color.Black, Color.White, Color.Gray)) }

    val coroutineScope = rememberCoroutineScope()

    paths.value.add(PathState(Path(), drawColor.value, drawBrush.value))

    // Function to handle form submission
    suspend fun handleFormSubmit() {
        if (userId != null) {
            isLoading = true
            try {
                val formattedDate = convertDateToApiFormat(selectedDate)

                val pengajuanRequest = PengajuanRequest(
                    tgl = formattedDate,
                    nama_sistem = namaSistem,
                    jenis = jenisSistem,
                    rencana_anggaran = rencanaAnggaran,
                    masalah = masalahSistem,
                    output = outputSistem,
                    status = "pending",  // or set based on other conditions
                    user_id = userId
                )

                // Convert the signature image to a file if available
                val signatureFile = image.value?.let { bitmap ->
                    val file = File(navController.context.cacheDir, "signature.png")
                    file.outputStream().use { out ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    }
                    file
                }

                if (signatureFile != null) {
                    // Make the API call to post the form data with signature file
                    val response = postPengajuan(pengajuanRequest, signatureFile)

                    // Check if the response was successful
                    if (response.status.value in 200..299) {
                        // Show Toast message on success
                        Toast.makeText(navController.context, "Pengajuan berhasil dikirim!", Toast.LENGTH_LONG).show()

                        // Navigate back to previous screen
                        navController.popBackStack()
                    } else {
                        // Show Toast message on failure
                        Toast.makeText(navController.context, "Gagal mengirim. Coba lagi!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Show Toast message if signature is missing
                    Toast.makeText(navController.context, "Tanda tangan diperlukan.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Show error message in case of failure
                Toast.makeText(navController.context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                isLoading = false
            }
        } else {
            // Show error message if userId is missing
            Toast.makeText(navController.context, "User ID is missing.", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        HeaderForm("Form Pembuatan Perangkat Lunak", navController)

        // Form Fields
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Date Picker
            DatePickerField(
                label = "Hari/Tanggal",
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            // Nama Sistem
            FormField(
                label = "Nama Sistem",
                placeholder = "Sistem baru",
                value = namaSistem,
                onValueChange = { namaSistem = it }
            )

            // Jenis Sistem
            DropdownField(
                label = "Jenis Sistem",
                options = listOf("sistem_baru", "pengembangan"),
                selectedOption = jenisSistem,
                onOptionSelected = { jenisSistem = it }
            )

            // Rencana Anggaran
            DropdownField(
                label = "Rencana Anggaran",
                options = listOf("termasuk_dalam_perencanaan", "tidak_termasuk_perencanaan"),
                selectedOption = rencanaAnggaran,
                onOptionSelected = { rencanaAnggaran = it }
            )

            // Masalah Sistem
            FormField(
                label = "Masalah pada Sistem yang Ada",
                placeholder = "Bug tampilan...",
                value = masalahSistem,
                onValueChange = { masalahSistem = it }
            )

            // Output Sistem
            FormField(
                label = "Output/Hasil yang Diharapkan",
                placeholder = "Hasil yang diinginkan...",
                value = outputSistem,
                onValueChange = { outputSistem = it }
            )

            // Pemohon Section
            Text(
                text = "Pemohon:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Maroon
            )

            // Tombol untuk menambahkan tanda tangan
            Button(
                onClick = { isDialogOpen.value = true },
                modifier = Modifier
                    .width(150.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                shape = RoundedCornerShape(15.dp) // Mengatur sudut membulat lebih kecil
            ) {
                Text(
                    text = "Insert TTD",
                    color = Putih,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            SignatureDialog(
                isDialogOpen = isDialogOpen,
                capturingViewBound = capturingViewBounds,
                drawColor = drawColor,
                drawBrush = drawBrush,
                usedColors = usedColors,
                paths = paths,
                image = image
            )

            if (image.value != null) {
                Image(
                    bitmap = image.value!!.asImageBitmap(),
                    contentDescription = "Capture Image"
                )
            }

            // Menampilkan hasil tanda tangan yang sudah dipilih
            if (!paths.value.isEmpty()) {
                Text("Tanda Tangan Anda:", color = Maroon)
            }

            // Submit Button
            Row(
                modifier = Modifier
                    .fillMaxWidth() // Memastikan Row memanfaatkan lebar penuh
                    .padding(start = 16.dp, bottom = 16.dp, end = 16.dp), // Padding agar tombol tidak menempel pada tepi layar
                horizontalArrangement = Arrangement.End // Mengatur agar tombol berada di kanan
            ) {
                ActionButton(
                    onClick = {
                        coroutineScope.launch {
                            handleFormSubmit()
                        }
                    },
                    buttonType = "submit" // This will create a "Submit" button
                )
            }
            if (isLoading) {
                // Show loading indicator
                Text("Submitting...", fontSize = 18.sp, color = Maroon)
            }
        }
    }
}




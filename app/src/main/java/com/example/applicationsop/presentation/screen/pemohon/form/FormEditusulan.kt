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
import com.example.applicationsop.Api.updatePengajuan
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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormEditUsulan(
    navController: NavController,
    id: String?,
    nama_Sistem: String?,
    hari_Tanggal: String?,
    jenis_Sistem: String?,
    rencana_Anggaran: String?,
    masalah_Sistem: String?,
    output_Hasil: String?,
    status: String?,
    alasan_penolakan: String?
) {
    // State untuk menyimpan inputan form
    var namaSistem by remember { mutableStateOf(nama_Sistem ?: "") }
    var jenisSistem by remember { mutableStateOf(jenis_Sistem ?: "") }
    var rencanaAnggaran by remember { mutableStateOf(rencana_Anggaran ?: "") }
    var masalahSistem by remember { mutableStateOf(masalah_Sistem ?: "") }
    var outputSistem by remember { mutableStateOf(output_Hasil ?: "") }
    var selectedDate by remember { mutableStateOf(hari_Tanggal ?: "") }

    // To show success or error messages
    var isLoading by remember { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf("") }

    // Signature Pad
    val paths = remember { mutableStateOf(mutableListOf<PathState>()) }
    val drawColor = remember { mutableStateOf(Color.Black) }
    val drawBrush = remember { mutableStateOf(5f) }

    val coroutineScope = rememberCoroutineScope()

    paths.value.add(PathState(Path(), drawColor.value, drawBrush.value))

    // Function to handle form submission
    suspend fun handleFormSubmit() {
        if (id != null) {
            isLoading = true
            try {

                val pengajuanRequest = PengajuanRequest(
                    nama_sistem = namaSistem,
                    jenis = jenisSistem,
                    rencana_anggaran = rencanaAnggaran,
                    masalah = masalahSistem,
                    output = outputSistem,
                    status = "pending",
                    alasan_penolakan = null

                )
                // Call the API to update the Pengajuan data
                coroutineScope.launch {
                    try {
                        val response = updatePengajuan(id, pengajuanRequest)

                        // Check if the response was successful
                        if (response.status.value in 200..299) {
                            Toast.makeText(navController.context, "Edit Pengajuan berhasil diperbarui!", Toast.LENGTH_SHORT).show()
//                            onDismiss()
                        } else {
                            Toast.makeText(navController.context, "Gagal memperbarui. Coba lagi!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(navController.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    } finally {
                        isLoading = false
                    }
                }
            } catch (e: Exception) {
                responseMessage = "Error: ${e.message}"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        HeaderForm("Form Update Permohonan Perangkat Lunak", navController)

        // Form Fields
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Date Picker (Read-only version)
            DatePickerField2(
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

            // Submit Button
            Row(
                modifier = Modifier
                    .fillMaxWidth() // Memastikan Row memanfaatkan lebar penuh
                    .padding(
                        start = 16.dp,
                        bottom = 16.dp,
                        end = 16.dp
                    ), // Padding agar tombol tidak menempel pada tepi layar
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

            if (responseMessage.isNotEmpty()) {
                Text(
                    responseMessage,
                    fontSize = 18.sp,
                    color = if (responseMessage.contains("success")) Color.Green else Color.Red
                )
            }
        }
    }
}

@Composable
fun DatePickerField2(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Display the selected date as a Text view
        Text(
            text = selectedDate,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .background(Color.LightGray)
                .padding(16.dp)
                .fillMaxWidth()
        )

        // Optionally, add a visual indicator that this field is not editable (e.g., a "view-only" tag)
    }
}




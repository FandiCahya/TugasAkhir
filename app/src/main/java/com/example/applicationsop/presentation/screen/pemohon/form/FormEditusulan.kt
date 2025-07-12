package com.example.applicationsop.presentation.screen.pemohon.form

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.Api.updatePengajuan
import com.example.applicationsop.models.Pengajuan
import com.example.applicationsop.models.PengajuanRequest
import com.example.applicationsop.presentation.component.ActionButton
import com.example.applicationsop.presentation.component.DropdownField
import com.example.applicationsop.presentation.component.FormField
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.presentation.component.signaturepad.PathState
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import io.ktor.client.content.LocalFileContent
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormEditUsulan(
    navController: NavController,
    id: String?
) {
    val context = LocalContext.current
    var detail by remember { mutableStateOf<Pengajuan?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    // State form field
    val namaSistemState = remember { mutableStateOf("") }
    val jenisSistemState = remember { mutableStateOf("") }
    val rencanaAnggaranState = remember { mutableStateOf("") }
    val masalahSistemState = remember { mutableStateOf("") }
    val outputSistemState = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf("") }

    LaunchedEffect(id) {
        if (id != null) {
            val list = fetchPengajuanList(context, status = null, role = "user", devisi = "")
            detail = list.find { it.id == id }
            detail?.let {
                namaSistemState.value = it.nama_sistem ?: ""
                jenisSistemState.value = it.jenis ?: ""
                rencanaAnggaranState.value = it.rencana_anggaran ?: ""
                masalahSistemState.value = it.masalah ?: ""
                outputSistemState.value = it.output ?: ""
                selectedDate.value = it.tgl ?: ""
            }
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    suspend fun handleFormSubmit() {
        if (id != null) {
            isLoading = true
            coroutineScope.launch {
                try {
                    val pengajuanRequest = PengajuanRequest(
                        nama_sistem = namaSistemState.value,
                        jenis = jenisSistemState.value,
                        rencana_anggaran = rencanaAnggaranState.value,
                        masalah = masalahSistemState.value,
                        output = outputSistemState.value,
                        status = "pending",
                        alasan_penolakan = null
                    )
                    val response = updatePengajuan(context, id, pengajuanRequest)
                    if (response.status.value in 200..299) {
                        Toast.makeText(context, "Edit Pengajuan berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Gagal memperbarui. Coba lagi!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(rememberScrollState())
    ) {
        HeaderForm("Edit Pengajuan", navController)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .fillMaxHeight(), // Pastikan column bisa "mendorong" ke bawah
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DatePickerField2(
                label = "Hari/Tanggal",
                selectedDate = selectedDate.value,
                onDateSelected = { selectedDate.value = it }
            )

            FormField("Nama Sistem", "Sistem baru", namaSistemState.value) {
                namaSistemState.value = it
            }

            DropdownField(
                label = "Jenis Sistem",
                options = listOf("sistem_baru", "pengembangan"),
                selectedOption = jenisSistemState.value,
                onOptionSelected = { jenisSistemState.value = it }
            )

            DropdownField(
                label = "Rencana Anggaran",
                options = listOf("termasuk_dalam_perencanaan", "tidak_termasuk_perencanaan"),
                selectedOption = rencanaAnggaranState.value,
                onOptionSelected = { rencanaAnggaranState.value = it }
            )

            FormField("Masalah pada Sistem yang Ada", "Bug tampilan...", masalahSistemState.value) {
                masalahSistemState.value = it
            }

            FormField("Output/Hasil yang Diharapkan", "Hasil yang diinginkan...", outputSistemState.value) {
                outputSistemState.value = it
            }

            // Tambahkan Spacer untuk mendorong button ke bawah
            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ActionButton(
                    onClick = { coroutineScope.launch { handleFormSubmit() } },
                    buttonType = "submit"
                )
            }

            if (isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = Maroon, strokeWidth = 3.dp)
                }
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
    }
}




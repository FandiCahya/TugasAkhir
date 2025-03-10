package com.example.applicationsop.presentation.screen.admin.form

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.navigation.NavController
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.presentation.component.DatePickerField
import com.example.applicationsop.presentation.component.FormField
import com.example.applicationsop.models.PengembanganRequest
import com.example.applicationsop.Api.postPengembangan
import com.example.applicationsop.data.ScheduleItem
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.presentation.screen.pemohon.form.convertDateToApiFormat

@Composable
fun ScheduleForm(
    navController: NavController,
    id: String?,
    namaSistem: String?,
    onSave: (ScheduleItem) -> Unit
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedStages by remember { mutableStateOf(listOf<String>()) }
    var progressPercentage by remember { mutableStateOf(0) }

    val availableStages = listOf("Analisis", "Desain UI/UX", "Pengerjaan", "Penyelesaian", "Testing")

    val scrollState = rememberScrollState()
    var isLoading by remember { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf("") }
    var validationErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    // Create a trigger state to launch the effect
    val triggerApiCall = remember { mutableStateOf(false) }

    // Fungsi validasi
    fun validateForm(): Boolean {
        var errors = mutableMapOf<String, String>()

        // Validasi Tanggal Mulai dan Tanggal Selesai
        if (startDate.isEmpty()) {
            errors["startDate"] = "Tanggal mulai tidak boleh kosong."
        }
        if (endDate.isEmpty()) {
            errors["endDate"] = "Tanggal selesai tidak boleh kosong."
        }
        if (startDate.isNotEmpty() && endDate.isNotEmpty() && startDate > endDate) {
            errors["dateRange"] = "Tanggal mulai tidak boleh lebih besar dari tanggal selesai."
        }

        // Validasi Keterangan
        if (description.isEmpty()) {
            errors["description"] = "Keterangan tidak boleh kosong."
        }

        // Validasi Tahap Pengerjaan
        if (selectedStages.isEmpty()) {
            errors["stages"] = "Pilih setidaknya satu tahap pengerjaan."
        }

        // Jika ada error, tampilkan pesan
        validationErrors = errors
        return errors.isEmpty()
    }

    // Form fields and layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(scrollState)
    ) {
        // Header Section
        HeaderForm("Jadwal Pengembangan", navController)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var isFocused by remember { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxWidth()) {
                // Label
                Text(
                    text = "Perangkat yang dikembangkan:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp) // Spacing between label and content
                )

                // Box to display the device name
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp) // Ensure consistent height for the box
                        .border(
                            width = 1.dp,
                            color = Color.LightGray, // Border color
                            shape = RoundedCornerShape(8.dp) // Rounded corners for a softer look
                        )
                        .padding(horizontal = 8.dp) // Horizontal padding for the text inside
                ) {
                    Text(
                        text = "$namaSistem", // Display the device name
                        fontSize = 16.sp,
                        color = Color.Black, // Text color
                        modifier = Modifier
                            .align(Alignment.CenterStart) // Align text to the left
                            .padding(8.dp) // Padding for the text inside the box
                    )
                }
            }


            // Use the imported DatePickerField
            DatePickerField(
                label = "Tanggal Mulai",
                selectedDate = startDate,
                onDateSelected = { startDate = it }
            )
            validationErrors["startDate"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }

            DatePickerField(
                label = "Tanggal Selesai",
                selectedDate = endDate,
                onDateSelected = { endDate = it }
            )
            validationErrors["endDate"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }

            // Use the imported FormField for Keterangan
            FormField(
                label = "Keterangan",
                placeholder = "Isi keterangan",
                value = description,
                onValueChange = { description = it }
            )
            validationErrors["description"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stages Selection
        Text(
            "Pilih Tahap Pengerjaan:",
            fontWeight = FontWeight.Bold,
            color = Maroon,
            modifier = Modifier.padding(start = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Left Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                availableStages.take(3).forEach { stage ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedStages.contains(stage),
                            onCheckedChange = { isChecked ->
                                selectedStages = if (isChecked) {
                                    selectedStages + stage
                                } else {
                                    selectedStages - stage
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Maroon, // Warna centang Maroon
                                uncheckedColor = Color.LightGray, // Warna checkbox saat tidak dicentang
                                checkmarkColor = Color.White // Warna tanda centang itu sendiri
                            )
                        )
                        Text(stage, modifier = Modifier.padding(start = 8.dp), color = Maroon)
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                availableStages.drop(3).forEach { stage ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedStages.contains(stage),
                            onCheckedChange = { isChecked ->
                                selectedStages = if (isChecked) {
                                    selectedStages + stage
                                } else {
                                    selectedStages - stage
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Maroon, // Warna centang Maroon
                                uncheckedColor = Color.LightGray, // Warna checkbox saat tidak dicentang
                                checkmarkColor = Color.White // Warna tanda centang itu sendiri
                            )
                        )
                        Text(stage, modifier = Modifier.padding(start = 8.dp), color = Maroon)
                    }
                }
            }
        }

        // Tampilkan pesan error jika tahap pengerjaan tidak dipilih
        validationErrors["stages"]?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 20.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = {
                    if (validateForm()) {
                        triggerApiCall.value = true
                    }
                },
                modifier = Modifier
                    .size(120.dp, 40.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Simpan", color = Color.White)
            }
        }

        if (isLoading) {
            Text("Mengirim data...", color = Maroon, fontSize = 18.sp)
        }

        if (responseMessage.isNotEmpty()) {
            Text(
                responseMessage,
                color = if (responseMessage.contains("berhasil")) Color.Green else Color.Red,
                fontSize = 18.sp
            )
        }
    }

    val formattedStartDate = convertDateToApiFormat(startDate)
    val formattedEndDate = convertDateToApiFormat(endDate)

    if (triggerApiCall.value) {
        val pengembanganRequest = PengembanganRequest(
            pengajuan_id = id ?: "",
            tanggal_mulai = formattedStartDate,
            tanggal_selesai = formattedEndDate,
            tahap = selectedStages.joinToString(", "),
            persentase = progressPercentage,
            keterangan = description,
            status = "developed"
        )

        LaunchedEffect(triggerApiCall.value) {
            isLoading = true
            try {
                val response = postPengembangan(pengembanganRequest)
                if (response.status.value in 200..299) {
                    responseMessage = "Pengembangan berhasil disubmit!"
                    // Show a Toast after successful submission
                    Toast.makeText(navController.context, "Pengembangan berhasil!", Toast.LENGTH_SHORT).show()
                    // Navigate to the pengembanganAdmin screen
                    navController.navigate("pengembanganAdmin")
                } else {
                    responseMessage = "Gagal mengirim Pengembangan!"
                }
            } catch (e: Exception) {
                responseMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}

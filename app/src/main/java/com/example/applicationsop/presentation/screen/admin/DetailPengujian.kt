package com.example.applicationsop.presentation.screen.admin

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.applicationsop.Api.postPengujian
import com.example.applicationsop.models.PengujianRequest
import com.example.applicationsop.presentation.component.DatePickerField
import com.example.applicationsop.presentation.component.FormField
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.presentation.component.signaturepad.PathState
import com.example.applicationsop.presentation.component.signaturepad.SignatureDialog
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

fun getUserData(context: Context): Map<String, String?> {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
    val token = sharedPreferences.getString("TOKEN", null)
    val userId = sharedPreferences.getString("USER_ID", null)
    val role = sharedPreferences.getString("ROLE", null)
    val name = sharedPreferences.getString("NAME", null)
    val email = sharedPreferences.getString("EMAIL", null)
    val devisi = sharedPreferences.getString("DEVISI", null)

    return mapOf(
        "token" to token,
        "userId" to userId,
        "role" to role,
        "name" to name,
        "email" to email,
        "devisi" to devisi
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun DetailPengujianAdmin(navController: NavController, idPengembangan: String?, namaSistem: String?) {
    // Informasi Pengujian
    var versiPerangkat by remember { mutableStateOf("") }
    var tujuanPengujian by remember { mutableStateOf("") }
    var metodePengujian by remember { mutableStateOf("") }
    var tanggalPengujian by remember { mutableStateOf("") }

    // State untuk input uraian
    var selectedTestType by remember { mutableStateOf("positif") } // Pilihan uji default adalah "positif"
    var namaUji by remember { mutableStateOf("") }
    var kasusUji by remember { mutableStateOf("") }
    var hasilYangDiharapkan by remember { mutableStateOf("") }
    var hasilPengujian by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    // Validation State
    var showValidationError by remember { mutableStateOf(false) }
    var validationErrorMessage by remember { mutableStateOf("") }

    // Date formatting
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val onDateSelected: (String) -> Unit = { date ->
        val parsedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date)
        tanggalPengujian = parsedDate?.let { dateFormat.format(it) } ?: ""
    }

    val coroutineScope = rememberCoroutineScope()
    val context = navController.context
    val userData = getUserData(context)
    val userId = userData["userId"]

    val scrollState = rememberScrollState()

    // Validation function
    fun validateForm(): Boolean {
        return when {
            versiPerangkat.isEmpty() -> {
                validationErrorMessage = "Versi perangkat lunak tidak boleh kosong!"
                false
            }
            tujuanPengujian.isEmpty() -> {
                validationErrorMessage = "Tujuan pengujian tidak boleh kosong!"
                false
            }
            metodePengujian.isEmpty() -> {
                validationErrorMessage = "Metode pengujian tidak boleh kosong!"
                false
            }
            tanggalPengujian.isEmpty() -> {
                validationErrorMessage = "Tanggal pengujian tidak boleh kosong!"
                false
            }
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(scrollState)
    ) {
        // Header Section
        HeaderForm(title = "Formulir Pengujian", navController)

        // Form Fields Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Informasi Pengujian
            Text(text = "Informasi Pengujian", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Divider(color = Color.Gray, thickness = 1.dp)

            FormField(
                label = "Versi perangkat lunak",
                placeholder = "Isi versi perangkat lunak",
                value = versiPerangkat,
                onValueChange = { versiPerangkat = it })
            FormField(
                label = "Tujuan Pengujian",
                placeholder = "Isi tujuan pengujian",
                value = tujuanPengujian,
                onValueChange = { tujuanPengujian = it })
            FormField(
                label = "Metode Pengujian",
                placeholder = "Isi metode pengujian",
                value = metodePengujian,
                onValueChange = { metodePengujian = it })
            DatePickerField(
                label = "Tanggal Pengujian",
                selectedDate = tanggalPengujian,
                onDateSelected = onDateSelected,
                showLabel = false
            )

            // Display Validation Error
            if (showValidationError) {
                Text(
                    text = validationErrorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Submit Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        if (validateForm()) {
                            val pengujianRequest = PengujianRequest(
                                pengembangan_id = idPengembangan,
                                perangkat_lunak = namaSistem,
                                versi = versiPerangkat,
                                tujuan = tujuanPengujian,
                                metode = metodePengujian,
                                tanggal = tanggalPengujian,
                                pelaksana_id = userId,
                                nama_uji = namaUji,
                                kasus_uji = kasusUji,
                                hasil_diharapkan = hasilYangDiharapkan,
                                hasil_pengujian = hasilPengujian,
                                status = keterangan,
                                jenis_uji = selectedTestType
                            )
                            val jsonPayload = Json.encodeToString(pengujianRequest)
                            // Post the data
                            coroutineScope.launch {
                                try {
                                    postPengujian(pengujianRequest)
                                    Toast.makeText(
                                        context,
                                        "Pengujian berhasil disubmit!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Terjadi kesalahan, coba lagi!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            showValidationError = true
                        }
                    },
                    modifier = Modifier
                        .width(125.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = "Submit",
                        color = Putih,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

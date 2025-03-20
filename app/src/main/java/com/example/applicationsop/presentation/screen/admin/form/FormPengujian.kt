package com.example.applicationsop.presentation.screen.admin.form

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.example.applicationsop.Api.fetchUserList
import com.example.applicationsop.Api.postPengujian
import com.example.applicationsop.models.CatatanPengujian
import com.example.applicationsop.models.PengujianDetail
import com.example.applicationsop.models.PengujianRequest
import com.example.applicationsop.models.Users
import com.example.applicationsop.presentation.component.DatePickerField
import com.example.applicationsop.presentation.component.FormField
import com.example.applicationsop.presentation.component.UserCheckboxList
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
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormPengujianAdmin(navController: NavController, idPengembangan: String?, namaSistem: String?) {
    // Informasi Pengujian
    var versiPerangkat by remember { mutableStateOf("") }
    var tujuanPengujian by remember { mutableStateOf("") }
    var metodePengujian by remember { mutableStateOf("") }
    var tanggalPengujian by remember { mutableStateOf("") }
    var UraianCatatan by remember { mutableStateOf("") }
    var rencanacatatan by remember { mutableStateOf("") }

    val checkedUserIds = remember { mutableStateOf(emptyList<String>()) }
//    var pelaksanaPengujian by remember { mutableStateOf("") }

    // Date formatting
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // When a date is selected, format it to yyyy-MM-dd
    val onDateSelected: (String) -> Unit = { date ->
        val parsedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date)
        tanggalPengujian = parsedDate?.let { dateFormat.format(it) } ?: ""
    }

    // Signature Pad state
    val paths = remember { mutableStateOf(mutableListOf<PathState>()) }
    val capturingViewBounds = remember { mutableStateOf<Rect?>(null) }
    val image = remember { mutableStateOf<Bitmap?>(null) }
    val isDialogOpen = remember { mutableStateOf(false) }
    val drawColor = remember { mutableStateOf(Color.Black) }
    val drawBrush = remember { mutableStateOf(5f) }
    val usedColors = remember { mutableStateOf(mutableSetOf(Color.Black, Color.White, Color.Gray)) }

    // catatan
    var showCatatan by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Membuat scrollable column
    val scrollState = rememberScrollState()

    // Validation state
    var validationErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    // State untuk input uraian
    var selectedKeterangan by remember { mutableStateOf("uji_positif") } // Pilihan uji default adalah "positif"
    var namaUji by remember { mutableStateOf("") }
    var kasusUji by remember { mutableStateOf("") }
    var hasilYangDiharapkan by remember { mutableStateOf("") }
    var hasilPengujian by remember { mutableStateOf("") }
    var keteranganStatus by remember { mutableStateOf("OK") }

    // Get user data (userId)
    val context = navController.context
    val userData = getUserData(context)
    val userId = userData["userId"]

//    println("UserId pengujian$userId")

    paths.value.add(PathState(Path(), drawColor.value, drawBrush.value))

    fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()

        // Validasi Versi perangkat
        if (versiPerangkat.isEmpty()) {
            errors["versiPerangkat"] = "Versi perangkat tidak boleh kosong"
        }
        // Validasi Tujuan pengujian
        if (tujuanPengujian.isEmpty()) {
            errors["tujuanPengujian"] = "Tujuan pengujian tidak boleh kosong"
        }
        // Validasi Metode pengujian
        if (metodePengujian.isEmpty()) {
            errors["metodePengujian"] = "Metode pengujian tidak boleh kosong"
        }
        // Validasi Tanggal pengujian
        if (tanggalPengujian.isEmpty()) {
            errors["tanggalPengujian"] = "Tanggal pengujian tidak boleh kosong"
        }
        // Validasi input uraian
        if (namaUji.isEmpty()) errors["namaUji"] = "Nama uji tidak boleh kosong"
        if (kasusUji.isEmpty()) errors["kasusUji"] = "Kasus uji tidak boleh kosong"
        if (hasilYangDiharapkan.isEmpty()) errors["hasilYangDiharapkan"] =
            "Hasil yang diharapkan tidak boleh kosong"
        if (hasilPengujian.isEmpty()) errors["hasilPengujian"] =
            "Hasil pengujian tidak boleh kosong"
        if (keteranganStatus.isEmpty()) errors["keterangan"] = "Keterangan tidak boleh kosong"

        // Perbarui pesan error
        validationErrors = errors
        return errors.isEmpty()  // Jika tidak ada error, form valid
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
            // Judul Form Informasi Pengujian
            Text(
                text = "Informasi Pengujian",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Garis Pemisah untuk Membantu Visualisasi
            Divider(color = Color.Gray, thickness = 1.dp)

            Column(modifier = Modifier.fillMaxWidth()) {
                // Label
                Text(
                    text = "Perangkat yang dikembangkan:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
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

            FormField(
                label = "Versi perangkat lunak",
                placeholder = "Isi versi perangkat lunak",
                value = versiPerangkat,
                onValueChange = { versiPerangkat = it })
            validationErrors["versiPerangkat"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }
            FormField(
                label = "Tujuan Pengujian",
                placeholder = "Isi tujuan pengujian",
                value = tujuanPengujian,
                onValueChange = { tujuanPengujian = it })
            validationErrors["tujuanPengujian"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }
            FormField(
                label = "Metode Pengujian",
                placeholder = "Isi metode pengujian",
                value = metodePengujian,
                onValueChange = { metodePengujian = it })
            validationErrors["metodePengujian"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }
            DatePickerField(
                label = "Tanggal Pengujian",
                selectedDate = tanggalPengujian,
                onDateSelected = onDateSelected,
                showLabel = false
            )
            validationErrors["tanggalPengujian"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }

//           FormField(label = "Pelaksana Pengujian", placeholder = "Isi pelaksana pengujian", value = pelaksanaPengujian, onValueChange = { pelaksanaPengujian = it })

            Spacer(modifier = Modifier.height(0.dp))

            // Judul Form Informasi Pengujian
            Text(
                text = "Uraian Pengujian",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Garis Pemisah untuk Membantu Visualisasi
            Divider(color = Color.Gray, thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp), // Menambah jarak antar elemen
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Uji Positif RadioButton
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedKeterangan == "uji_positif",
                        onClick = { selectedKeterangan = "uji_positif" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Maroon,  // Warna centang yang aktif
                            unselectedColor = Color.LightGray  // Warna checkbox yang tidak aktif
                        )
                    )
                    Text(
                        "Uji Positif",
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.Black
                    )
                }

                // Pemisah
                Text("|", color = Color.Black, fontSize = 18.sp)

                // Uji Negatif RadioButton
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedKeterangan == "uji_negatif",
                        onClick = { selectedKeterangan = "uji_negatif" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Maroon,  // Warna centang yang aktif
                            unselectedColor = Color.LightGray  // Warna checkbox yang tidak aktif
                        )
                    )
                    Text(
                        "Uji Negatif",
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.Black
                    )
                }
            }

            FormField(
                label = "Nama Uji",
                placeholder = "Isi nama uji",
                value = namaUji,
                onValueChange = { namaUji = it })
            validationErrors["namaUji"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }
            FormField(
                label = "Kasus Uji",
                placeholder = "Isi kasus uji",
                value = kasusUji,
                onValueChange = { kasusUji = it })
            validationErrors["kasusUji"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }
            FormField(
                label = "Hasil Yang Diharapkan",
                placeholder = "Isi hasil yang diharapkan",
                value = hasilYangDiharapkan,
                onValueChange = { hasilYangDiharapkan = it })
            validationErrors["hasilYangDiharapkan"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }
            FormField(
                label = "Hasil Pengujian",
                placeholder = "Isi hasil pengujian",
                value = hasilPengujian,
                onValueChange = { hasilPengujian = it })
            validationErrors["hasilPengujian"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp), // Menambah jarak antar elemen
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Uji Positif RadioButton
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = keteranganStatus == "OK",
                        onClick = { keteranganStatus = "OK" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Maroon,  // Warna centang yang aktif
                            unselectedColor = Color.LightGray  // Warna checkbox yang tidak aktif
                        )
                    )
                    Text("Ok", modifier = Modifier.padding(start = 8.dp), color = Color.Black)
                }

                // Pemisah
                Text("|", color = Color.Black, fontSize = 18.sp)

                // Uji Negatif RadioButton
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = keteranganStatus == "Tidak",
                        onClick = { keteranganStatus = "Tidak" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Maroon,  // Warna centang yang aktif
                            unselectedColor = Color.LightGray  // Warna checkbox yang tidak aktif
                        )
                    )
                    Text("Not Ok", modifier = Modifier.padding(start = 8.dp), color = Color.Black)
                }
            }
            validationErrors["keteranganStatus"]?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = if (showCatatan) "Sembunyikan" else "Tambah Catatan",
                    color = Color.Blue, // Menggunakan warna biru agar terlihat interaktif
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { showCatatan = !showCatatan } // Toggle state saat diklik
                        .padding(8.dp) // Padding agar tidak terlalu dekat dengan teks
                        .background(
                            color = Color.LightGray.copy(alpha = 0.2f), // Background ringan yang memberi efek interaktif
                            shape = RoundedCornerShape(8.dp) // Sudut membulat
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ) // Padding untuk memberi ruang pada teks
                )
            }

            // Tampilkan hanya jika showCatatan bernilai true
            if (showCatatan) {
                Text(
                    text = "Catatan Pengujian",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Divider(modifier = Modifier.padding(top = 8.dp))

                FormField(
                    label = "Uraian",
                    placeholder = "Isi uraian",
                    value = UraianCatatan,
                    onValueChange = { UraianCatatan = it })
                FormField(
                    label = "Rencana Tindak Lanjut",
                    placeholder = "Isi rencana tindak lanjut",
                    value = rencanacatatan,
                    onValueChange = { rencanacatatan = it })
            }

            Divider(modifier = Modifier.padding(bottom = 0.dp))

            UserCheckboxList(checkedUserIds = checkedUserIds)

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
                            val pengujianDetails = listOf(
                                PengujianDetail(
                                    nama_uji = namaUji,
                                    kasus_uji = kasusUji,
                                    hasil_diharapkan = hasilYangDiharapkan,
                                    hasil_pengujian = hasilPengujian,
                                    kategori = selectedKeterangan,
                                    status = keteranganStatus
                                )
                            )

                            // Buat objek catatan_pengujian jika showCatatan aktif
                            val catatanPengujian = if (showCatatan) {
                                CatatanPengujian(
                                    uraian = UraianCatatan,
                                    rencana_tindak_lanjut = rencanacatatan,
                                    penanggung_jawab_id = userId,
                                )
                            } else {
                                null
                            }
                            println("showCatatan: $showCatatan")
                            println("catatanPengujian: $catatanPengujian")


                            val pengujianRequest = PengujianRequest(
                                pengembangan_id = idPengembangan,
                                perangkat_lunak = namaSistem,
                                versi = versiPerangkat,
                                tujuan = tujuanPengujian,
                                metode = metodePengujian,
                                tanggal = tanggalPengujian,
                                pelaksana_id = userId,
                                user_ids = checkedUserIds.value,  // Pastikan list user ID benar
                                pengujian_detail = pengujianDetails,
                                catatan = catatanPengujian // Tambahkan catatan pengujian jika ada
                            )

                            val jsonPayload = Json.encodeToString(pengujianRequest)
                            println("Request payload: $jsonPayload")

                            coroutineScope.launch {
                                try {
                                    postPengujian(pengujianRequest)
                                    Toast.makeText(context, "Pengujian berhasil disubmit!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .width(115.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Submit", color = Color.White)
                }
            }
        }
    }
}

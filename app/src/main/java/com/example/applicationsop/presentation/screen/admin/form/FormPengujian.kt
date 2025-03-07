package com.example.applicationsop.presentation.screen.admin.form

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.focus.focusModifier
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
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormPengujianAdmin(navController: NavController, idPengembangan: String?, namaSistem: String?) {
    // Informasi Pengujian
    var versiPerangkat by remember { mutableStateOf("") }
    var tujuanPengujian by remember { mutableStateOf("") }
    var metodePengujian by remember { mutableStateOf("") }
    var tanggalPengujian by remember { mutableStateOf("") }
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

    // Get user data (userId)
    val context = navController.context
    val userData = getUserData(context)
    val userId = userData["userId"]

    println("UserId pengujian$userId")

    paths.value.add(PathState(Path(), drawColor.value, drawBrush.value))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(scrollState)
    ) {
        // Header Section
        HeaderForm(title = "Formulir Pengujian", navController        )


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
                showLabel = false // Hide the label
            )

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

            FormField(
                label = "Nama Uji",
                placeholder = "Isi nama uji",
                value = versiPerangkat,
                onValueChange = { versiPerangkat = it })
            FormField(
                label = "Kasus Uji",
                placeholder = "Isi kasus uji",
                value = tujuanPengujian,
                onValueChange = { tujuanPengujian = it })
            FormField(
                label = "Hasil Yang Diharapkan",
                placeholder = "Isi hasil yang diharapkan",
                value = metodePengujian,
                onValueChange = { metodePengujian = it })
            FormField(
                label = "Hasil Pengujian",
                placeholder = "Isi hasil pengujian",
                value = metodePengujian,
                onValueChange = { metodePengujian = it })

            // Tombol untuk menampilkan/menghilangkan catatan
            Button(
                onClick = { showCatatan = !showCatatan }, // Toggle state saat diklik
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showCatatan) "Sembunyikan Catatan" else "Tambah Catatan")
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
                    value = versiPerangkat,
                    onValueChange = { versiPerangkat = it })
                FormField(
                    label = "Rencana Tindak Lanjut",
                    placeholder = "Isi rencana tindak lanjut",
                    value = tujuanPengujian,
                    onValueChange = { tujuanPengujian = it })
                FormField(
                    label = "Penanggung Jawab",
                    placeholder = "Isi penanggung jawab",
                    value = metodePengujian,
                    onValueChange = { metodePengujian = it })
            }

            // Tanda tangan
            Button(
                onClick = {
                    try {
                        isDialogOpen.value = true
                    } catch (e: Exception) {
                        Log.e("FormPengujianAdmin", "Error opening Signature Dialog: ${e.message}")
                    }
                },
                modifier = Modifier
                    .width(150.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = "Insert TTD",
                    color = Putih,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Signature Dialog
            if (isDialogOpen.value) {
                SignatureDialog(
                    isDialogOpen = isDialogOpen,
                    capturingViewBound = capturingViewBounds,
                    drawColor = drawColor,
                    drawBrush = drawBrush,
                    usedColors = usedColors,
                    paths = paths,
                    image = image
                )
            }

            if (image.value != null) {
                Image(
                    bitmap = image.value!!.asImageBitmap(),
                    contentDescription = "Capture Image"
                )
            }

            if (!paths.value.isEmpty()) {
                Text("Tanda Tangan Penguji:", color = Maroon)
            }

            // Submit Button
            Row(
                modifier = Modifier.fillMaxWidth().padding(end = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        val pengujianRequest = PengujianRequest(
                            pengembangan_id = idPengembangan,
                            perangkat_lunak = namaSistem,
                            versi = versiPerangkat,
                            tujuan = tujuanPengujian,
                            metode = metodePengujian,
                            tanggal = tanggalPengujian,
                            pelaksana_id = userId // Using userId from getUserData
                        )
                        val jsonPayload = Json.encodeToString(pengujianRequest)
                        println("Request payload: $jsonPayload")
                        // Call the API to post the Pengujian data
                        coroutineScope.launch {
                            try {
                                postPengujian(pengujianRequest)
                                // Show success Toast
                                Toast.makeText(context, "Pengujian berhasil disubmit!", Toast.LENGTH_SHORT).show()
                                // Navigate back after successful submission
                                navController.popBackStack()
                            } catch (e: Exception) {
                                // Show error Toast
                                Toast.makeText(context, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(115.dp) // Width for the Submit button
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


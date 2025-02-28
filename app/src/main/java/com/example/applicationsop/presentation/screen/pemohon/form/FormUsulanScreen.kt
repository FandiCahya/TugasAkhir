package com.example.applicationsop.presentation.screen.pemohon.form

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.applicationsop.Api.postPengajuan
import com.example.applicationsop.models.PengajuanRequest
import com.example.applicationsop.presentation.component.ActionButton
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import kotlinx.coroutines.launch
import java.util.Calendar

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

@Composable
fun FormUsulanScreen(navController: NavController, userId: String?) {
    // Menyimpan tanda tangan
    Log.d("FormUsulanScreen", "userId: $userId")
    var signaturePath by remember { mutableStateOf(Path()) }
    var showSignaturePad by remember { mutableStateOf(false) }
    var showSignatureValidDialog by remember { mutableStateOf(false) }

    // State untuk menyimpan inputan form
    var namaSistem by remember { mutableStateOf("") }
    var jenisSistem by remember { mutableStateOf("") }
    var rencanaAnggaran by remember { mutableStateOf("") }
    var masalahSistem by remember { mutableStateOf("") }
    var outputSistem by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }

    // To show success or error messages
    var isLoading by remember { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Function to handle form submission
    suspend fun handleFormSubmit() {
        if (userId != null) {
            isLoading = true
            try {
//              Log the form data before submitting it
//                Log.d("FormUsulanScreen", "Submitting form data:")
//                Log.d("FormUsulanScreen", "Tanggal: $selectedDate")
//                Log.d("FormUsulanScreen", "Nama Sistem: $namaSistem")
//                Log.d("FormUsulanScreen", "Jenis Sistem: $jenisSistem")
//                Log.d("FormUsulanScreen", "Rencana Anggaran: $rencanaAnggaran")
//                Log.d("FormUsulanScreen", "Masalah: $masalahSistem")
//                Log.d("FormUsulanScreen", "Output: $outputSistem")

                // Format tanggal sesuai dengan yang diinginkan API
                val formattedDate = convertDateToApiFormat(selectedDate)

                // Create PengajuanRequest from form data
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

                // Make the API call
                val response = postPengajuan(pengajuanRequest)

                // Check if the response was successful
                if (response.status.value in 200..299) {
                    responseMessage = "Pengajuan submitted successfully!"
                } else {
                    responseMessage = "Failed to submit Pengajuan. Please try again."
                }
            } catch (e: Exception) {
                responseMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        } else {
            responseMessage = "User ID is missing."
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
    ) {
        // Header
        HeaderComposableFormUsulan("Form Pembuatan Perangkat Lunak", navController)

        // Form Fields
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hari/Tanggal dengan DatePicker
                DatePickerField(
                    label = "Hari/Tanggal",
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )

            // Update FormField to use namaSistem state
            FormField(
                label = "Nama Sistem",
                placeholder = "Sistem baru",
                value = namaSistem,
                onValueChange = { namaSistem = it }
            )

            // Update DropdownField to use jenisSistem state
            DropdownField(
                label = "Jenis Sistem",
                options = listOf("sistem_baru", "pengembangan"),
                selectedOption = jenisSistem,
                onOptionSelected = { jenisSistem = it }
            )

            DropdownField(
                label = "Rencana Anggaran",
                options = listOf("termasuk_dalam_perencanaan", "tidak_termasuk_perencanaan"),
                selectedOption = rencanaAnggaran,
                onOptionSelected = { rencanaAnggaran = it }
            )

            FormField(
                label = "Masalah pada Sistem yang Ada",
                placeholder = "Bug tampilan...",
                value = masalahSistem,
                onValueChange = { masalahSistem = it }
            )

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
                onClick = { showSignaturePad = true },
                modifier = Modifier
                    .width(150.dp)
                    .padding(8.dp),
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

            // Dialog untuk signature pad
            if (showSignaturePad) {
                SignatureDialog(
                    onDismiss = {
                        showSignaturePad = false
                    },
                    onSignatureComplete = { path ->
                        signaturePath = path
                        showSignatureValidDialog = true // Tampilkan dialog validasi
                    },
                    onClearSignature = { signaturePath = Path() } // Clear signature if needed
                )
            }

            // Menampilkan hasil tanda tangan yang sudah dipilih
            if (!signaturePath.isEmpty) {
                Text("Tanda Tangan Anda:")
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    drawPath(
                        path = signaturePath,
                        color = Color.Black,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
            }

            // Submit Button
            Row(
                modifier = Modifier
                    .fillMaxWidth() // Memastikan Row memanfaatkan lebar penuh
                    .padding(16.dp), // Padding agar tombol tidak menempel pada tepi layar
                horizontalArrangement = Arrangement.End // Mengatur agar tombol berada di kanan
            ) {
                ActionButton(
                    onClick = {
                        // Launch the coroutine when the button is clicked
                        coroutineScope.launch {
                            handleFormSubmit()
                        }
                    },
                    buttonType = "submit" // This will create a "Submit" button
                )
            }
            // Display Loading Indicator or Response Message
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
fun HeaderComposableFormUsulan(title: String, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Maroon, Color.White.copy(alpha = 0f)),
                    startX = 0f,
                    endX = Float.POSITIVE_INFINITY
                )
            )
            .padding(16.dp)
    ) {
        // Header with Back Button and Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, // Vertically align the items
            horizontalArrangement = Arrangement.Start // Align items to the start (left)
        ) {
            BackButton(
                navController = navController,
                colorVersion = "w"
            )  // Back Button on the left

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Putih,
                textAlign = TextAlign.Center // Ensure the title is centered
            )
        }
    }
}

@Composable
fun DatePickerField(label: String, selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { onDateSelected(it) },  // Update the state when the text changes
        label = { Text(label) },
        placeholder = { Text("Pilih Tanggal") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDatePicker(context) { date ->
                    onDateSelected(date) // Update the state when the date is selected
                }
            },
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Maroon,
            unfocusedBorderColor = Color.LightGray
        )
    )
}


fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Menampilkan dialog DatePicker
    DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            // Format tanggal yang dipilih dan memanggil onDateSelected
            val formattedDate =
                String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            onDateSelected(formattedDate)  // Mengirim tanggal yang dipilih
        },
        year,
        month,
        day
    ).show()
}

@Composable
fun FormField(label: String, placeholder: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) }, // Update the state when the text changes
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Maroon,
            unfocusedBorderColor = Color.LightGray
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { /* Handle next */ })
    )
}

@Composable
fun DropdownField(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            placeholder = { Text("Pilih $label") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Maroon,
                unfocusedBorderColor = Color.LightGray
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.zIndex(1f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)  // Update state when option is selected
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun SignaturePad(onSignatureComplete: (Path) -> Unit) {
    // Declare the path and a state to track the drawing
    var path by remember { mutableStateOf(Path()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                // Detect drag gestures for drawing the signature
                detectDragGestures { change, dragAmount ->
                    change.consume()  // Consume the touch event to prevent scroll
                    path = path.apply {
                        // Update the path with each movement
                        lineTo(change.position.x, change.position.y)
                    }
                }
            }
    ) {
        // Draw the signature on the canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPath(
                path = path,
                color = Color.Black,  // Color of the signature
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}


@Composable
fun SignatureDialog(
    onDismiss: () -> Unit,
    onSignatureComplete: (Path) -> Unit,
    onClearSignature: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Center
                ) {
                    Text(
                        text = "Tambahkan Tanda Tangan",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    // Close Button at the top-right
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(35.dp)
                            .background(Color.White, CircleShape)
                            .padding(8.dp)
                            .zIndex(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // SignaturePad Section
                    Box(
                        modifier = Modifier
                            .background(
                                Color.Gray.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp)
                    ) {
                        SignaturePad(onSignatureComplete = onSignatureComplete)
                    }

                    // Button Row below the signature pad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onClearSignature,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                        ) {
                            Text("Clear", color = Color.Black)
                        }

                        Button(
                            onClick = {
                                onSignatureComplete(Path())
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Maroon)
                        ) {
                            Text("Save", color = Color.White)
                        }
                    }
                }
            },
            confirmButton = { },
            dismissButton = { },
        )
    }
}

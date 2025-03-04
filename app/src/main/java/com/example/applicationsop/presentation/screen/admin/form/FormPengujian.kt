package com.example.applicationsop.presentation.screen.admin.form

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.applicationsop.presentation.component.ActionButton
import com.example.applicationsop.presentation.component.DatePickerField
import com.example.applicationsop.presentation.component.FormField
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.presentation.component.signaturepad.PathState
import com.example.applicationsop.presentation.component.signaturepad.SignatureDialog
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.PinkTua
import com.example.applicationsop.ui.theme.Putih
import java.util.Calendar

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormPengujianAdmin(navController: NavController, namaSistem: String)  {
    var versiPerangkat by remember { mutableStateOf("") }
    var tujuanPengujian by remember { mutableStateOf("") }
    var metodePengujian by remember { mutableStateOf("") }
    var tanggalPengujian by remember { mutableStateOf("") }
    var pelaksanaPengujian by remember { mutableStateOf("") }

    // Signature Pad state
    val paths = remember { mutableStateOf(mutableListOf<PathState>()) }
    val capturingViewBounds = remember { mutableStateOf<Rect?>(null) }
    val image = remember { mutableStateOf<Bitmap?>(null) }
    val isDialogOpen = remember { mutableStateOf(false) } // Single state for dialog visibility
    val drawColor = remember { mutableStateOf(Color.Black) }
    val drawBrush = remember { mutableStateOf(5f) }
    val usedColors = remember { mutableStateOf(mutableSetOf(Color.Black, Color.White, Color.Gray)) }

    // Membuat scrollable column
    val scrollState = rememberScrollState()

    paths.value.add(PathState(Path(), drawColor.value, drawBrush.value))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(scrollState)
    ) {
        // Header Section
        HeaderForm("Formulir Pengujian", navController)

        // Form Fields Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            FormField(label = "Versi perangkat lunak", placeholder = "Isi versi perangkat lunak", value = versiPerangkat, onValueChange = { versiPerangkat = it })
            FormField(label = "Tujuan Pengujian", placeholder = "Isi tujuan pengujian", value = tujuanPengujian, onValueChange = { tujuanPengujian = it })
            FormField(label = "Metode Pengujian", placeholder = "Isi metode pengujian", value = metodePengujian, onValueChange = { metodePengujian = it })

            DatePickerField(
                label = "Tanggal Pengujian",
                selectedDate = tanggalPengujian,
                onDateSelected = { tanggalPengujian = it },
                showLabel = false // Hide the label
            )

            FormField(label = "Pelaksana Pengujian", placeholder = "Isi pelaksana pengujian", value = pelaksanaPengujian, onValueChange = { pelaksanaPengujian = it })

            // Penguji Section
            Text(
                text = "Penguji:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Maroon
            )

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
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    drawPath(
                        path = paths.value.last().path, // Mengambil path terakhir yang digambar
                        color = Color.Black,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
            }

            // Submit Button with Catatan and Uraian buttons to the left
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Uniform space between buttons
            ) {
                // Row for the smaller buttons (Catatan and Uraian)
                Row(
                    modifier = Modifier.weight(1f), // This makes sure the Row takes available space
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between buttons
                ) {
                    // Button for Catatan
                    Button(
                        onClick = { /* Handle Catatan click */ },
                        modifier = Modifier
                            .width(100.dp) // Maintain the same width for uniformity
                            .shadow(4.dp, RoundedCornerShape(16.dp)), // Apply shadow to all buttons
                        colors = ButtonDefaults.buttonColors(containerColor = PinkTua),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Catatan", color = Color.White)
                    }

                    // Button for Uraian
                    Button(
                        onClick = { /* Handle Uraian click */ },
                        modifier = Modifier
                            .width(100.dp) // Maintain the same width for uniformity
                            .shadow(4.dp, RoundedCornerShape(16.dp)), // Apply shadow to all buttons
                        colors = ButtonDefaults.buttonColors(containerColor = PinkTua),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Uraian", color = Color.White)
                    }
                }

                // Submit Button
                Button(
                    onClick = { /* Handle submit action */ },
                    modifier = Modifier
                        .width(115.dp) // Width for the Submit button
                        .shadow(4.dp, RoundedCornerShape(16.dp)), // Apply shadow to Submit button
                    colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Submit", color = Color.White)
                }
            }
        }
    }
}


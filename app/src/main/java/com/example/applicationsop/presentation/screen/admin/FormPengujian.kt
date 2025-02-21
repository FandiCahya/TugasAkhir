package com.example.applicationsop.presentation.screen.admin

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.LinearGradient
import android.app.DatePickerDialog
import android.content.Context
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.applicationsop.presentation.component.ActionButton
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.ui.theme.ijo
import java.util.Calendar

@Composable
fun FormPengujianAdmin(navController: NavController) {
    var perangkat by remember { mutableStateOf("") }
    var versiPerangkat by remember { mutableStateOf("") }
    var tujuanPengujian by remember { mutableStateOf("") }
    var metodePengujian by remember { mutableStateOf("") }
    var tanggalPengujian by remember { mutableStateOf("") }
    var pelaksanaPengujian by remember { mutableStateOf("") }
    var showSignatureDialog by remember { mutableStateOf(false) }
    var catatan by remember { mutableStateOf("") }
    var uraian by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih) // Ensure the background is set here
    ) {
        // Header Section
        HeaderComposableFormUsulan("Formulir Pengujian", navController)

        // Form Fields Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Form fields
            FormField(label = "Perangkat yang di Uji", placeholder = "Isi perangkat yang diuji")
            FormField(label = "Versi perangkat lunak", placeholder = "Isi versi perangkat lunak")
            FormField(label = "Tujuan Pengujian", placeholder = "Isi tujuan pengujian")
            FormField(label = "Metode Pengujian", placeholder = "Isi metode pengujian")
            DatePickerField(label = "Tanggal Pengujian")
            FormField(label = "Pelaksana Pengujian", placeholder = "Isi pelaksana pengujian")

            // Row for buttons (Insert TTD, Catatan, Uraian)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp), // Adjust spacing between buttons
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Insert TTD Button
                Button(
                    onClick = { showSignatureDialog = true },
                    modifier = Modifier
                        .height(40.dp) // Adjust button height for smaller size
                        .weight(1f), // Make buttons occupy equal width
                    colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Insert TTD", color = Color.White, fontSize = 14.sp)
                }

                // Catatan Button
                Button(
                    onClick = { /* Handle Catatan Click */ },
                    modifier = Modifier
                        .height(40.dp) // Adjust button height
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Catatan", color = Color.White, fontSize = 14.sp)
                }

                // Uraian Button
                Button(
                    onClick = { /* Handle Uraian Click */ },
                    modifier = Modifier
                        .height(40.dp) // Adjust button height
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Uraian", color = Color.White, fontSize = 14.sp)
                }
            }

            // Submit Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ActionButton(
                    onClick = { /* Handle the submit action */ },
                    buttonType = "submit" // Adjust for your form submission action
                )
            }
        }
    }

    // Signature Dialog for Insert TTD
    if (showSignatureDialog) {
        SignatureDialog(
            onDismiss = { showSignatureDialog = false },
            onSignatureComplete = { /* Handle signature complete */ },
            onClearSignature = { /* Handle clear signature */ }
        )
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BackButton(
                navController = navController,
                colorVersion = "w"
            )

            Text(
                text = title,
                Modifier.padding(start = 45.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Putih,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FormField(label: String, placeholder: String) {
    var value by remember { mutableStateOf("") }
    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
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
fun DatePickerField(label: String) {
    var selectedDate by remember { mutableStateOf("") }
    val context = LocalContext.current

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { selectedDate = it },
        label = { Text(label) },
        placeholder = { Text("Pilih Tanggal") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDatePicker(context) { date -> selectedDate = date }
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

    DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            onDateSelected(formattedDate)
        },
        year,
        month,
        day
    ).show()
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
                    SignaturePad(onSignatureComplete = onSignatureComplete)

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

@Composable
fun SignaturePad(onSignatureComplete: (Path) -> Unit) {
    var path by remember { mutableStateOf(Path()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    path = path.apply { lineTo(change.position.x, change.position.y) }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}

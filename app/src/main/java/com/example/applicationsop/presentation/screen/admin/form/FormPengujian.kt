package com.example.applicationsop.presentation.screen.admin.form

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.presentation.component.ActionButton
import com.example.applicationsop.presentation.component.DatePickerField
import com.example.applicationsop.presentation.component.FormField
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.presentation.component.signaturepad.PathState
import com.example.applicationsop.presentation.component.signaturepad.SignatureDialog
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import java.util.Calendar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormPengujianAdmin(navController: NavController, namaSistem: String)  {
    var perangkat by remember { mutableStateOf("") }
    var versiPerangkat by remember { mutableStateOf("") }
    var tujuanPengujian by remember { mutableStateOf("") }
    var metodePengujian by remember { mutableStateOf("") }
    var tanggalPengujian by remember { mutableStateOf("") }
    var pelaksanaPengujian by remember { mutableStateOf("") }
    var showSignatureDialog by remember { mutableStateOf(false) }
    var catatan by remember { mutableStateOf("") }
    var uraian by remember { mutableStateOf("") }

    // Membuat scrollable column
    val scrollState = rememberScrollState()

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
            isDialogOpen = remember { mutableStateOf(true) },
            capturingViewBound = remember { mutableStateOf(null) },
            drawColor = remember { mutableStateOf(Color.Black) },
            drawBrush = remember { mutableStateOf(4f) },
            usedColors = remember { mutableStateOf(mutableSetOf<Color>()) },
            paths = remember { mutableStateOf(mutableListOf<PathState>()) },
            image = remember { mutableStateOf(null) }
        )
    }
}


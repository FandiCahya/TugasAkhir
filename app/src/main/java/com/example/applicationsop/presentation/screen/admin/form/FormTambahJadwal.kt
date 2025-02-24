package com.example.applicationsop.presentation.screen.admin.form

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.applicationsop.data.ScheduleItem
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import java.util.Calendar


@Composable
fun ScheduleForm(navController: NavController, onSave: (ScheduleItem) -> Unit) {
    var taskName by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedStages by remember { mutableStateOf(listOf<String>()) }
    var progressPercentage by remember { mutableStateOf(0) }

    val availableStages = listOf("Analisis", "Desain UI/UX", "Pengerjaan", "Penyelesaian", "Testing")

    // Membuat scrollable column
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(scrollState)
    ) {
        // Header Section
        HeaderComposableFormPengujian("Tambah Jadwal Pengembangan", navController)

        // Form Fields Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Form fields
            FormField(label = "Perangkat yang dikembangkan", placeholder = "Isi perangkat yang dikembangkan") {
                taskName = it
            }

            DatePickerField(label = "Tanggal Mulai") {
                startDate = it
            }

            DatePickerField(label = "Tanggal Selesai") {
                endDate = it
            }

            FormField(label = "Keterangan", placeholder = "Isi keterangan") {
                description = it
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stages Selection with two columns
        Text(
            "Pilih Tahap Pengerjaan:",
            fontWeight = FontWeight.Bold,
            color = Maroon,
            modifier = Modifier.padding(start = 16.dp) // Added padding to the left
        )

        // Create two columns for the checkboxes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Left Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                availableStages.take(3).forEach { stage ->  // First three stages
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedStages.contains(stage),
                            onCheckedChange = { isChecked ->
                                selectedStages = if (isChecked) {
                                    selectedStages + stage
                                } else {
                                    selectedStages - stage
                                }
                            }
                        )
                        Text(
                            stage,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Maroon // Set the text color to Maroon
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                availableStages.drop(3).forEach { stage ->  // Last two stages
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedStages.contains(stage),
                            onCheckedChange = { isChecked ->
                                selectedStages = if (isChecked) {
                                    selectedStages + stage
                                } else {
                                    selectedStages - stage
                                }
                            }
                        )
                        Text(
                            stage,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Maroon // Set the text color to Maroon
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button at the bottom right with shadow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = {
                    val schedule = ScheduleItem(
                        task = taskName,
                        startDate = startDate,
                        endDate = endDate,
                        description = description,
                        stage = selectedStages.joinToString(", "),
                        progressPercentage = progressPercentage
                    )
                    onSave(schedule)
                    navController.popBackStack() // Kembali ke layar sebelumnya
                },
                modifier = Modifier
                    .size(120.dp, 40.dp) // Resize button to make it smaller
                    .shadow(4.dp, RoundedCornerShape(16.dp)), // Adding shadow to the button
                colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Simpan", color = Color.White)
            }
        }
    }
}


@Composable
fun FormField(label: String, placeholder: String, onValueChange: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        },
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
fun DatePickerField(label: String, onDateSelected: (String) -> Unit) {
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
                showDatePicker(context) { date ->
                    selectedDate = date
                    onDateSelected(date)
                }
            },
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Maroon,
            unfocusedBorderColor = Color.LightGray
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showDatePickerAdd(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate =
                String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            onDateSelected(formattedDate)
        },
        year,
        month,
        day
    ).show()
}


@Composable
fun HeaderComposableFormAddSchedule(title: String, navController: NavController) {
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


package com.example.applicationsop.presentation.component

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.ui.theme.Maroon
import java.util.Calendar

@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    isError: Boolean = false
) {
    val context = LocalContext.current
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Label
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Date Picker Box without placeholder and black background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDatePicker(context) { date ->
                        onDateSelected(date)  // Update the state when the date is selected
                    }
                }
                .height(56.dp) // Ensure consistent height
                .border(
                    width = 1.dp,
                    color = if (isError) Color.Red else if (isFocused) Maroon else Color.LightGray, // Use Maroon when focused, otherwise LightGray
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp) // Padding for inside text
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused // Track focus state
                }
        ) {
            Text(
                text = if (selectedDate.isNotEmpty()) selectedDate else "Pilih Tanggal",
                fontSize = 16.sp,
                color = Color.Black, // Set the text color to black
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

// Function to show Date Picker Dialog
fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            onDateSelected(formattedDate)  // Send selected date
        },
        year,
        month,
        day
    ).show()
}
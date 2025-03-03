package com.example.applicationsop.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.applicationsop.ui.theme.Maroon

@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    isError: Boolean = false
) {
    // Create a map for user-friendly display names
    val displayNames = mapOf(
        "sistem_baru" to "Sistem Baru",
        "pengembangan" to "Pengembangan",
        "termasuk_dalam_perencanaan" to "Termasuk Anggaran",
        "tidak_termasuk_perencanaan" to "Tidak Termasuk Anggaran"
    )

    var expanded by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Label
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)  // Spacing between label and selected option
        )

        // Box for displaying selected option without placeholder or black background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .height(56.dp) // Ensure consistent height
                .border(
                    width = 2.dp,
                    color = if (isError) Color.Red else if (isFocused) Maroon else Color.LightGray, // Use Maroon when focused, otherwise LightGray
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused // Track focus state
                }
        ) {
            Text(
                text = if (selectedOption.isNotEmpty())
                    displayNames[selectedOption] ?: selectedOption
                else "Pilih $label",
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier.align(Alignment.CenterStart).padding(8.dp)
            )
        }

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.zIndex(1f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(displayNames[option] ?: option) }, // Display the user-friendly name
                    onClick = {
                        onOptionSelected(option)  // Update state when option is selected
                        expanded = false
                    }
                )
            }
        }
    }
}
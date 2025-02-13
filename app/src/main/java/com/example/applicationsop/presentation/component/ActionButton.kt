package com.example.applicationsop.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.ui.theme.Maroon // Import Maroon color
import com.example.applicationsop.ui.theme.PinkPudar // Import PinkPudar color

// Function to create various action buttons like Edit, Save, etc.
@Composable
fun ActionButton(
    onClick: () -> Unit, // Action when button is clicked
    buttonType: String // Define the type of button to adjust color and text
) {
    // Define colors and text based on buttonType
    val buttonColor = when (buttonType) {
        "edit" -> Maroon
        "save" -> PinkPudar
        "reject" -> Color.Red
        "submit" -> Maroon
        "send" -> Color.Green
        "finish" -> Color.Blue
        else -> Color.Gray // Default color
    }

    val buttonText = when (buttonType) {
        "edit" -> "Edit"
        "save" -> "Save"
        "reject" -> "Reject"
        "submit" -> "Submit"
        "send" -> "Send"
        "finish" -> "Finish"
        else -> "Action"
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .height(40.dp)
            .shadow(2.dp, shape = RoundedCornerShape(15.dp), clip = false) // Add shadow with offset
            .offset(x = -3.dp, y = -2.dp), // Offset shadow to the bottom-right
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor), // Set dynamic color
        shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp) // Rounded corners
    ) {
        Text(
            text = buttonText,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

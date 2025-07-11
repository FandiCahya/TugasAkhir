package com.example.applicationsop.loginScreen.ComponentLogin

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.applicationsop.R
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih

@Composable
fun TextInputComposable(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    iconId: Int,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityChange: (Boolean) -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 25.dp, end = 25.dp),
        singleLine = true,
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        leadingIcon = {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = Maroon
            )
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { onPasswordVisibilityChange(!isPasswordVisible) }) {
                    Icon(
                        painter = painterResource(id = if (isPasswordVisible) R.drawable.ic_closeeye else R.drawable.ic_openeye),
                        contentDescription = "Toggle Password Visibility",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Maroon,
            focusedLabelColor = Maroon,
            focusedLeadingIconColor = Maroon,
            focusedTrailingIconColor = Maroon,
            unfocusedBorderColor = Color.Gray,
            unfocusedLabelColor = Color.Gray,
            unfocusedLeadingIconColor = Putih,
            unfocusedTrailingIconColor = Color.Gray
        ),
        textStyle = TextStyle(color = Color.Black) // Set the text color to black
    )
}
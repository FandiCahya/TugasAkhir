package com.example.applicationsop.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter

@Composable
fun FilterComponent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(139.dp)
            .requiredHeight(96.dp)
            .background(Color.White)
            .border(BorderStroke(1.dp, Color(0xffa9a9a9)))
    ) {
        Text(
            text = "Filters",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 12.sp,
                letterSpacing = 1.2.sp
            ),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 6.dp, start = 9.dp)
        )

        Text(
            text = "Sort By",
            color = Color(0xff445a8c),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp
            ),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 35.dp, start = 10.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 55.dp, start = 11.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = true, // Or set dynamically based on your state
                    onClick = {},
                    modifier = Modifier.size(9.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Terbaru",
                    color = Color(0xff4e67a0),
                    style = TextStyle(
                        fontSize = 12.sp,
                        letterSpacing = 1.2.sp
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 19.dp)
            ) {
                RadioButton(
                    selected = false, // Or set dynamically based on your state
                    onClick = {},
                    modifier = Modifier.size(9.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Terlama",
                    color = Color(0xff4e67a0),
                    style = TextStyle(
                        fontSize = 12.sp,
                        letterSpacing = 1.2.sp
                    )
                )
            }
        }
    }
}

@Preview(widthDp = 139, heightDp = 96)
@Composable
private fun FilterComponentPreview() {
    FilterComponent(modifier = Modifier)
}

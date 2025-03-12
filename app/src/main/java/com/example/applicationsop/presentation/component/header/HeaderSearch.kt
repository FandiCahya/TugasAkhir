package com.example.applicationsop.presentation.component.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.applicationsop.presentation.component.BackButton
import com.example.applicationsop.ui.theme.Maroon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderWithSearch(
    navController: NavController,
    title: String, // Dynamically change the title for each screen
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Maroon, Color.Transparent), // Gradasi dari Maroon ke Transparan
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .zIndex(1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(
                navController = navController,
                colorVersion = "w",
            )

            Spacer(modifier = Modifier.width(80.dp)) // Space between BackButton and the title

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Search bar positioned below the title and center it vertically
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter) // Align the search bar at the bottom and center horizontally
//                .padding(horizontal = 50.dp)
//                .padding(bottom = 30.dp)
//        ) {
//            TextField(
//                value = "",
//                onValueChange = { /* Handle text input here */ },
//                placeholder = {
//                    Text(
//                        text = "Cari",
//                        color = Color.Gray, // Adjust color as needed
//                        style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
//                    )
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Filled.Search,
//                        contentDescription = "Search",
//                        tint = Maroon,
//                        modifier = Modifier.padding(start = 20.dp)
//                    )
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(45.dp),
//                shape = RoundedCornerShape(50.dp),
////                colors = TextFieldDefaults.textFieldColors(
////                    containerColor = Color.White,
////                    focusedIndicatorColor = Color.Transparent,  // Remove the focus indicator line
////                    unfocusedIndicatorColor = Color.Transparent // Remove the unfocused indicator line
////                )
//            )
//        }
    }
}
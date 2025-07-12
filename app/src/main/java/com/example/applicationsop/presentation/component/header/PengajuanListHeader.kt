package com.example.applicationsop.presentation.component.header

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.ui.theme.PinkPudar

@Composable
fun PengajuanListHeader(
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    val filters = listOf("All", "Pending", "Rejected", "Accepted", "Developing", "Testing", "Finished")

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "List Pengajuan",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            filters.forEach { filter ->
                val isSelected = selectedFilter == filter
                FilterChipItem(
                    label = filter,
                    selected = isSelected,
                    onClick = { onFilterChange(filter) }
                )
            }
        }
    }
}

@Composable
fun FilterChipItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (selected) PinkPudar else Color(0xFFE0E0E0),
        shadowElevation = if (selected) 4.dp else 0.dp,
        modifier = Modifier
            .padding(end = 8.dp)
            .height(36.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 14.dp)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = if (selected) Color.White else Color(0xFF444444),
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

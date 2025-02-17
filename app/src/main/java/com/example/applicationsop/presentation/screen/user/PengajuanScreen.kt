package com.example.applicationsop.presentation.screen.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.PinkPudar
import com.example.applicationsop.ui.theme.Putih

@Composable
fun PengajuanScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var filterExpanded by remember { mutableStateOf(false) }
    val filterOptions = listOf("Terbaru", "Terlama")
    val submissions = listOf(
        SubmissionData("Sistem A", "Menunggu konfirmasi"),
        SubmissionData("Sistem B", "Ditolak"),
        SubmissionData("Sistem C", "Diterima"),
        SubmissionData("Sistem D", "Proses Pengembangan")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
    ) {
        // Header dengan search dan filter di kanan
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cari") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                modifier = Modifier
                    .weight(1f)  // Pastikan search bar mengambil ruang yang cukup
                    .padding(end = 8.dp), // Memberikan sedikit jarak antara search bar dan filter
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Putih,
                    unfocusedBorderColor = Putih
                )
            )

            // Filter button (kita bisa menggunakan icon untuk filter)
            IconButton(onClick = { filterExpanded = !filterExpanded }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Toggle Filter"
                )
            }
        }

        // List Pengajuan
        Text(
            text = "List Pengajuan",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = Maroon
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(submissions) { submission ->
                SubmissionItem(
                    name = submission.name,
                    status = submission.status
                )
            }
        }
    }
}

@Composable
fun HeaderComposable(
    title: String,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    filterExpanded: Boolean,
    filterOptions: List<String>,
    selectedFilter: String,
    onFilterSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Maroon)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Putih
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Cari") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Putih,
                    unfocusedBorderColor = Putih
                )
            )

            Box {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter Icon",
                        tint = Putih
                    )
                }

                DropdownMenu(
                    expanded = filterExpanded,
                    onDismissRequest = { onFilterClick() }
                ) {
                    filterOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onFilterSelect(option)
                                onFilterClick()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubmissionItem(name: String, status: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder image
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Gray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = android.R.drawable.sym_def_app_icon),
                    contentDescription = "Placeholder Image"
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = status,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

data class SubmissionData(
    val name: String,
    val status: String
)

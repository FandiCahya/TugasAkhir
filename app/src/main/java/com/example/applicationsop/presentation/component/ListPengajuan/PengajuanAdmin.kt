package com.example.applicationsop.presentation.component.ListPengajuan

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import com.example.applicationsop.presentation.component.SubmissionCard
import com.example.applicationsop.ui.theme.*

@Composable
fun SubmissionSectionAdmin(
    navController: NavController,
    pendingCount: Int,
    rejectedCount: Int,
    acceptedCount: Int,
    pengembanganCount: Int,
    pengujianCount: Int,
    finishedCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SubmissionCard(
            color = kuning,
            icon = Icons.Filled.Timer,
            label = "Pending",
            count = pendingCount,
            onClick = {
                navController.navigate("list_pengajuanAdmin1")
            }
        )

        SubmissionCard(
            color = abang,
            icon = Icons.Filled.Close,
            label = "Rejected",
            count = rejectedCount,
            onClick = {
                navController.navigate("list_pengajuanAdmin2")
            }
        )

        SubmissionCard(
            color = ijo,
            icon = Icons.Filled.Verified,
            label = "Accepted",
            count = acceptedCount,
            onClick = {
                navController.navigate("list_pengajuanAdmin3")
            }
        )

        SubmissionCard(
            color = biru,
            icon = Icons.Filled.Build,
            label = "Dev",
            count = pengembanganCount,
            onClick = {
                navController.navigate("pengembanganAdmin")
            }
        )

        SubmissionCard(
            color = BiruMuda,
            icon = Icons.Filled.Science,
            label = "Approval",
            count = pengujianCount,
            onClick = {
                navController.navigate("pengujianAdmin")
            }
        )

        SubmissionCard(
            color = Purple40,
            icon = Icons.Filled.Star,
            label = "Finished",
            count = finishedCount,
            onClick = {
                navController.navigate("historyAdmin")
            }
        )
    }
}
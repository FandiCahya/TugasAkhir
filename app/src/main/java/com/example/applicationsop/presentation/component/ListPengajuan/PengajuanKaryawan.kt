import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import com.example.applicationsop.presentation.component.SubmissionCard
import com.example.applicationsop.ui.theme.*

@Composable
fun SubmissionSection(
    navController: NavController,
    roleUS: String?,
    devisiUS: String?,
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
                navController.navigate("listUsulan1?role=$roleUS&devisi=$devisiUS")
            }
        )

        SubmissionCard(
            color = abang,
            icon = Icons.Filled.Close,
            label = "Rejected",
            count = rejectedCount,
            onClick = {
                navController.navigate("listUsulan3?role=$roleUS&devisi=$devisiUS")
            }
        )

        SubmissionCard(
            color = ijo,
            icon = Icons.Filled.Verified,
            label = "Accepted",
            count = acceptedCount,
            onClick = {
                navController.navigate("listUsulan2?role=$roleUS&devisi=$devisiUS")
            }
        )

        SubmissionCard(
            color = biru,
            icon = Icons.Filled.Build,
            label = "Dev",
            count = pengembanganCount,
            onClick = {
                navController.navigate("pengembanganUser")
            }
        )

        SubmissionCard(
            color = BiruMuda,
            icon = Icons.Filled.Science,
            label = "Approval",
            count = pengujianCount,
            onClick = {
                navController.navigate("pengujianUser")
            }
        )

        SubmissionCard(
            color = Purple40,
            icon = Icons.Filled.Star,
            label = "Finished",
            count = finishedCount,
            onClick = {
                navController.navigate("historyUser")
            }
        )
    }
}

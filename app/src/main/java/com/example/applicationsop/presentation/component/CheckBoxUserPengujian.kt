package com.example.applicationsop.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.Api.fetchUserList
import com.example.applicationsop.models.Users
import com.example.applicationsop.ui.theme.Maroon
import io.ktor.client.content.LocalFileContent
import kotlinx.coroutines.launch

@Composable
fun UserCheckboxList(checkedUserIds: MutableState<List<String>>) {
    val coroutineScope = rememberCoroutineScope()
    var userList by remember { mutableStateOf<List<Users>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Role yang ingin ditampilkan
    val filteredRoles = listOf("admin", "user", "qmr", "kepalacabang")

    val context = LocalContext.current


    // Fetch user list from API when the composable is first launched
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val fetchedUsers = fetchUserList(context)
            userList = fetchedUsers.filter { it.role in filteredRoles } // Filter user by role
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Pilih 4 User :",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Maroon
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp)) // Loading indicator
        } else {
            // Display users by role
            filteredRoles.forEach { role ->
                val usersByRole = userList.filter { it.role == role }
                if (usersByRole.isNotEmpty()) {
                    val roleDisplay = when (role) {
                        "user" -> "Pemohon"
                        "kepalacabang" -> "Kepala Cabang"
                        else -> role.capitalize()
                    }
                    Text(
                        text = roleDisplay.capitalize(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Maroon,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    // Horizontal Scroll for each role's users
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState()) // Enable horizontal scrolling
                            .padding(bottom = 8.dp)
                    ) {
                        // Display users in two columns for each role
                        usersByRole.chunked(2).forEach { rowUsers ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                rowUsers.forEach { user ->
                                    UserCheckboxItem(
                                        user = user,
                                        checkedStates = remember { mutableStateOf(mapOf<String, Boolean>()) },
                                        checkedUserIds = checkedUserIds
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Pesan validasi jika kurang dari 4 user yang dipilih
        if (checkedUserIds.value.size < 4) {
            Text(
                text = "Silakan pilih 4 pengguna.",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun UserCheckboxItem(
    user: Users,
    checkedStates: MutableState<Map<String, Boolean>>,
    checkedUserIds: MutableState<List<String>>
) {
    val isDisabled = checkedUserIds.value.size >= 4 && !checkedUserIds.value.contains(user.id)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.45f) // Ensuring that each checkbox fits within the grid
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = checkedStates.value[user.id] == true,
                onCheckedChange = { isChecked ->
                    if (!isDisabled) { // Only allow checking if not disabled
                        checkedStates.value = checkedStates.value + (user.id to isChecked)

                        // Update daftar user_id yang dipilih
                        checkedUserIds.value = if (isChecked) {
                            (checkedUserIds.value + user.id).distinct()
                        } else {
                            checkedUserIds.value - user.id
                        }
                    }
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Maroon,
                    uncheckedColor = Color.LightGray,
                    checkmarkColor = Color.White
                ),
                enabled = !isDisabled // Disable checkbox when 4 users are selected
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between checkbox and text
            Text(
                text = user.name,
                fontSize = 16.sp,
                color = Maroon,
            )
        }
    }
}

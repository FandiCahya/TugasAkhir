package com.example.applicationsop.loginScreen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.applicationsop.R
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.loginScreen.ComponentLogin.LoginButtonComposable
import com.example.applicationsop.loginScreen.ComponentLogin.LogoComposable
import com.example.applicationsop.loginScreen.ComponentLogin.SubtitleTextComposable
import com.example.applicationsop.loginScreen.ComponentLogin.TextInputComposable
import com.example.applicationsop.loginScreen.ComponentLogin.TitleTextComposable


@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Putih
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Logo
            LogoComposable()

            Spacer(modifier = Modifier.height(80.dp))

            // Title Text
            TitleTextComposable()

            // Subtitle Text
            SubtitleTextComposable()

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Pindahkan ke sini
                TextInputComposable(
                    label = "Email",
                    value = email.value,
                    onValueChange = { email.value = it },
                    iconId = R.drawable.ic_home,
                    isPassword = false
                )

                TextInputComposable(
                    label = "Password",
                    value = password.value,
                    onValueChange = { password.value = it },
                    iconId = R.drawable.ic_key,
                    isPassword = true,
                    isPasswordVisible = passwordVisible.value,
                    onPasswordVisibilityChange = {
                        passwordVisible.value = it
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Row to make login button and forgot password link inline


                // Login Button
                    LoginButtonComposable(
                        email = email.value,
                        password = password.value,
                        navController = navController,
                    )

            }
        }
    }
}







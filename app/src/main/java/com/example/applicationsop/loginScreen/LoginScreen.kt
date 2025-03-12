package com.example.applicationsop.loginScreen

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.applicationsop.R
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.PinkPudar
import com.example.applicationsop.ui.theme.Putih
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.applicationsop.Api.loginUser
import androidx.compose.ui.text.TextStyle


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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Forgot password link
                    ForgotPasswordLinkComposable {
                        // Handle forgot password click
                    }

                    // Login Button
                    LoginButtonComposable(
                        email = email.value,
                        password = password.value,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun LogoComposable() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 40.dp,
                start = 25.dp
            ) // Beri sedikit padding agar tidak terlalu ke tepi layar
    ) {
        Image(
            painter = painterResource(id = R.drawable.lifemedia_logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(100.dp) // Sesuaikan ukuran jika terlalu besar
                .align(Alignment.TopStart) // Memastikan logo ada di kiri atas
        )
    }
}

@Composable
fun TitleTextComposable() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SOPilot",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            color = Maroon,

            )
    }
}


@Composable
fun SubtitleTextComposable() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "DIGITAL SOP MANAGEMENT APPLICATION",
            fontSize = 13.sp,
            color = PinkPudar,
            modifier = Modifier.padding(bottom = 180.dp)
        )
    }
}

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


@Composable
fun LoginButtonComposable(
    email: String,
    password: String,
    navController: NavController,
) {
    val context = LocalContext.current
    Button(
        onClick = {
            // Memanggil API login ketika tombol diklik
            CoroutineScope(Dispatchers.Main).launch {
                // Panggil fungsi login dari API
                val response = loginUser(email, password)

                // Cek apakah login berhasil
                if (response != null && response.token != null) {
                    // Login berhasil, simpan token
                    saveUserData(
                        context,
                        response.token,
                        response.user.id ?: "",
                        response.user.role ?: "",
                        response.user.name ?: "",
                        response.user.email ?: "",
                        response.user.devisi ?: ""
                    )
                    // Menentukan navigasi berdasarkan role
                    when (response.user.role) {
                        "admin" -> {
                            navController.navigate("homeAdmin/${response.token}/${response.user.id}/${response.user.role}/${response.user.name}/${response.user.email}/${response.user.devisi}")
                        }

                        "user" -> {
                            navController.navigate("homeUser/${response.token}/${response.user.id}/${response.user.role}/${response.user.name}/${response.user.email}/${response.user.devisi}")
                        }

                        "kepalacabang" -> {
                            navController.navigate("homeKacab/${response.token}/${response.user.id}/${response.user.role}/${response.user.name}/${response.user.email}/${response.user.devisi}")
                        }

                        "qmr" -> {
                            navController.navigate("homeQmr/${response.token}/${response.user.id}/${response.user.role}/${response.user.name}/${response.user.email}/${response.user.devisi}")
                        }

                        else -> {
                            Toast.makeText(context, "Role tidak dikenal.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // Anda bisa menyimpan token di SharedPreferences atau sesi lainnya jika diperlukan
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()

                } else {
                    // Jika login gagal, tampilkan pesan error
                    Toast.makeText(
                        context,
                        "Login failed, please try again.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 80.dp, end = 30.dp)
            .shadow(4.dp, shape = RoundedCornerShape(16.dp), clip = false),
        colors = ButtonDefaults.buttonColors(
            containerColor = Maroon
        )
    ) {
        Text("Sign In", color = Putih)
    }
}

@Composable
fun ForgotPasswordLinkComposable(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
    ) {
        Text("Lupa password?", fontSize = 12.sp, color = Gray)
    }
}

fun saveToken(context: Context, token: String) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("TOKEN", token)  // Save the token with the key "TOKEN"
    editor.apply()  // Commit changes
}

fun saveUserData(
    context: Context,
    token: String,
    userId: String,
    role: String,
    name: String,
    email: String,
    devisi: String
) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("TOKEN", token)  // Simpan token
    editor.putString("USER_ID", userId) // Simpan userId
    editor.putString("ROLE", role) // Simpan role
    editor.putString("NAME", name) // Simpan name
    editor.putString("EMAIL", email) // Simpan email
    editor.putString("DEVISI", devisi) // Simpan devisi
    editor.apply() // Commit perubahan
}






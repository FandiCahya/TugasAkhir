package com.example.applicationsop.loginScreen.ComponentLogin

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.applicationsop.Api.loginUser
import com.example.applicationsop.core.saveUserData
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                } else {
                    // Jika login gagal, tampilkan pesan error
                    Toast.makeText(
                        context,
                        "Login Gagal, Coba Lagi.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(16.dp), clip = false),
        colors = ButtonDefaults.buttonColors(
            containerColor = Maroon
        )
    ) {
        Text("Masuk", color = Putih)
    }
}
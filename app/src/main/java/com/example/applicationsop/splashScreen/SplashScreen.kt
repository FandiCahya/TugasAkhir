package com.example.applicationsop.splashScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.MainActivity
import com.example.applicationsop.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000), label = ""
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)

        // Fetch user data from SharedPreferences or Intent
        val userData = getUserData(context)
        val token = userData["token"]

        if (token != null) {
            // Pass token and user data to MainActivity
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("token", token)
                putExtra("userId", userData["userId"])
                putExtra("role", userData["role"])
                putExtra("name", userData["name"])
                putExtra("email", userData["email"])
                putExtra("devisi", userData["devisi"])
            }
            context.startActivity(intent)
        } else {
            // Navigate to login screen if no token found
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        (context as? Activity)?.finish()
    }

    Splash(alpha = alphaAnim.value)
}

fun getUserData(context: Context): Map<String, String?> {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
    val token = sharedPreferences.getString("TOKEN", null)
    val userId = sharedPreferences.getString("USER_ID", null)
    val role = sharedPreferences.getString("ROLE", null)
    val name = sharedPreferences.getString("NAME", null)
    val email = sharedPreferences.getString("EMAIL", null)
    val devisi = sharedPreferences.getString("DEVISI", null)

    return mapOf(
        "token" to token,
        "userId" to userId,
        "role" to role,
        "name" to name,
        "email" to email,
        "devisi" to devisi
    )
}


@Composable
fun Splash(
    modifier: Modifier = Modifier,
    alpha: Float
) {
    Surface(
        modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.lifemedia_logo),
                contentDescription = "logo_lifemedia",
                modifier = Modifier
                    .size(180.dp)
                    .alpha(alpha)
            )
        }
        Box(
            modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "Versi 1.0",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
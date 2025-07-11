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
import com.example.applicationsop.core.UserUtils.getUserData
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

        // Ambil data dari SharedPreferences
        val userData = getUserData(context)
        val token = userData["token"] as? String
        val isLoggedIn = userData["isLoggedIn"] as? Boolean ?: false

        print("Token awal inisiasi : $token, LoginStatus: $isLoggedIn")

        if (!token.isNullOrEmpty() && isLoggedIn) {
            // Jika login valid, arahkan ke MainActivity
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("token", token)
                putExtra("userId", userData["userId"] as? String)
                putExtra("role", userData["role"] as? String)
                putExtra("name", userData["name"] as? String)
                putExtra("email", userData["email"] as? String)
                putExtra("devisi", userData["devisi"] as? String)
            }
            context.startActivity(intent)
        } else {
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            // Jika belum login, arahkan ke LoginActivity
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        (context as? Activity)?.finish()
    }


    Splash(alpha = alphaAnim.value)
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
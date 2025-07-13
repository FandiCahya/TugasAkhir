package com.example.applicationsop.loginScreen.ComponentLogin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.applicationsop.R

@Composable
fun LogoComposable() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 10.dp,
                start = 1.dp
            ) // Beri sedikit padding agar tidak terlalu ke tepi layar
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_cicle),
            contentDescription = "logo",
            modifier = Modifier
                .size(50.dp) // Sesuaikan ukuran jika terlalu besar
                .align(Alignment.TopStart) // Memastikan logo ada di kiri atas
        )
    }
}
package com.example.applicationsop.presentation.component.signaturepad

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.applyCanvas
import com.example.applicationsop.ui.theme.Maroon
import io.ktor.websocket.Frame
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@Composable
fun SignatureDialog(
    isDialogOpen: MutableState<Boolean>,
    capturingViewBound: MutableState<android.graphics.Rect?>,
    drawColor: MutableState<Color>,
    drawBrush: MutableState<Float>,
    usedColors: MutableState<MutableSet<Color>>,
    paths: MutableState<MutableList<PathState>>,
    image: MutableState<Bitmap?>
) {
    if (isDialogOpen.value) {
        Dialog(
            onDismissRequest = { isDialogOpen.value = false }, // Set false to dismiss the dialog
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val view = LocalView.current
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(300.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(.9f)
                        .onGloballyPositioned { layoutCoordinates ->
                            val bounds = layoutCoordinates.boundsInRoot()
                            capturingViewBound.value = android.graphics.Rect(
                                bounds.left.roundToInt(),
                                bounds.top.roundToInt(),
                                bounds.right.roundToInt(),
                                bounds.bottom.roundToInt()
                            )
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                ) {
                    DrawingCanvas(
                        drawColor = drawColor,
                        drawBrush = drawBrush,
                        usedColors = usedColors,
                        paths = paths.value
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        horizontalArrangement = Arrangement.spacedBy(25.dp)
                    ) {
                        Button(
                            onClick = {
                                paths.value = mutableListOf()
                            },
                            modifier = Modifier.weight(0.4f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Maroon, // Mengubah warna tombol menjadi maroon
                            ),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp) // Shadow effect
                        ) {
                            Frame.run { Text(text = "Clear", color = Color.White) }
                        }

                        Button(
                            onClick = {
                                val bounds = capturingViewBound.value
                                if (bounds != null) {
                                    image.value = Bitmap.createBitmap(
                                        bounds.width(), bounds.height(),
                                        Bitmap.Config.ARGB_8888
                                    ).applyCanvas {
                                        translate(-bounds.left.toFloat(), -bounds.top.toFloat())
                                        view.draw(this)
                                    }
                                    isDialogOpen.value = false
                                }
                            },
                            modifier = Modifier.weight(0.4f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Maroon, // Mengubah warna tombol menjadi maroon
                            ),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp) // Shadow effect
                        ) {
                            Frame.run { Text(text = "Save", color = Color.White) }
                        }
                    }
                }
            }
        }
    }
}

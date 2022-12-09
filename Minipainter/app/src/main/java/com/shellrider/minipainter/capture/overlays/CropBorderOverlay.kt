package com.shellrider.minipainter.capture.overlays

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp

@Composable
fun CropBorderOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val rect = Rect(
            offset = Offset((size.width - 320.dp.toPx()) / 2, (size.height - 320.dp.toPx()) / 2),
            size = Size(320.dp.toPx(), 320.dp.toPx())
        )

        val clip = Path().apply {
            addRoundRect(
                RoundRect(
                    rect,
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
            )
        }
        clipPath(clip, clipOp = ClipOp.Difference) {
            drawRect(
                Color.hsl(0.0f, 0.0f, 0.0f, 0.6f),
            )
        }
        drawRoundRect(
            color = Color.White,
            size = Size(320.dp.toPx(), 320.dp.toPx()),
            topLeft = Offset((size.width - 320.dp.toPx()) / 2, (size.height - 320.dp.toPx()) / 2),
            style = Stroke(width = 3.dp.toPx()),
            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
        )
    }
}
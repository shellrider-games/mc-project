package com.shellrider.minipainter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.shellrider.minipainter.ui.theme.CardColor

@Composable
fun AddImageEntryCard(
    modifier: Modifier = Modifier,
    imagePath: String,
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 24.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = CardColor)

    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = rememberAsyncImagePainter(imagePath),
            contentDescription = "review of cropped image"
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 24.dp, top = 0.dp)
        ) {
            content()
        }
    }
}
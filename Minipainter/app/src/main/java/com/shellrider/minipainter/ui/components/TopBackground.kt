package com.shellrider.minipainter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shellrider.minipainter.ui.theme.MainColor

@Composable
fun TopBackground(
    text: String?,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .background(MainColor),
    ) {
        if (text != null) {
            OurHeading(
                modifier = Modifier.align(Alignment.Center),
                text = text
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxHeight()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

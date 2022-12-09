package com.shellrider.minipainter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shellrider.minipainter.ui.theme.MainColor

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = { }
) {
    Row(
        modifier = modifier
            .padding(
                start = 20.dp,
                top = 0.dp,
                end = 20.dp,
                bottom = 16.dp
            )
            .fillMaxWidth()
            .height(56.dp)
            .clip(shape = RoundedCornerShape(50.dp))
            .background(color = MainColor),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}
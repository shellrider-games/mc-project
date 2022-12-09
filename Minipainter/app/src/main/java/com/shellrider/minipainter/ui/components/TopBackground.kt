package com.shellrider.minipainter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shellrider.minipainter.ui.theme.MainColor

@Composable
fun TopBackground() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(128.dp)
        .background(MainColor)) {
    }
}

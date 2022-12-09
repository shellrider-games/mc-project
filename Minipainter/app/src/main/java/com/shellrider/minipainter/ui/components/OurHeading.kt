package com.shellrider.minipainter.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shellrider.minipainter.ui.theme.OnMainColor

@Composable
fun OurHeading(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = text,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.W700,
        fontSize = 32.sp,
        color = OnMainColor
    )
}
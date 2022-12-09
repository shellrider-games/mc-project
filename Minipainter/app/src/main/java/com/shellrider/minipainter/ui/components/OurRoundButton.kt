package com.shellrider.minipainter.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shellrider.minipainter.camera.executor
import com.shellrider.minipainter.camera.takePicture
import com.shellrider.minipainter.ui.theme.DisabledColor
import com.shellrider.minipainter.ui.theme.MainColor
import com.shellrider.minipainter.ui.theme.OnMainColor
import kotlinx.coroutines.launch

@Composable
fun OurRoundButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
    color: Color = MainColor,
    textColor: Color = OnMainColor,
    enabled: Boolean = true
){
    Button(
        modifier = modifier
            .wrapContentSize()
            .clip(shape = RoundedCornerShape(50.dp))
        ,
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
            contentColor = textColor,
            disabledBackgroundColor = DisabledColor
        ),
        enabled = enabled
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 20.dp
            ),
            text = text.uppercase(),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            letterSpacing = 1.25.sp
        )
    }
}
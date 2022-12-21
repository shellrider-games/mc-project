package com.shellrider.minipainter.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.shellrider.minipainter.datamodel.MiniatureWithLatestProgress
import com.shellrider.minipainter.filesystem.imageNameToPath
import com.shellrider.minipainter.ui.theme.CardColor
import com.shellrider.minipainter.ui.theme.LightTextColor
import java.io.File
import java.text.SimpleDateFormat

@Composable
fun MiniatureOverviewCard(
    context: Context,
    miniature: MiniatureWithLatestProgress,
    imageName: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        elevation = 0.dp,
        backgroundColor = CardColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            val imagePath = imageNameToPath(context, imageName)
            val imageFile = File(imagePath)
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = rememberAsyncImagePainter(imageFile),
                contentDescription = "primary image of ${miniature.miniature.name}"
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = miniature.miniature.name,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.W500,
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = SimpleDateFormat("dd.MM.yyyy")
                    .format(miniature.miniature.lastUpdated ?: ""),
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                color = LightTextColor
            )
        }
    }
}
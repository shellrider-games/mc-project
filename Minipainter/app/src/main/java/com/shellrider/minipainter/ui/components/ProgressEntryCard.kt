package com.shellrider.minipainter.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.shellrider.minipainter.datamodel.ProgressEntryWithImage
import com.shellrider.minipainter.filesystem.imageNameToPath
import com.shellrider.minipainter.ui.theme.CardColor
import com.shellrider.minipainter.ui.theme.LightTextColor
import java.text.SimpleDateFormat

@Composable
fun ProgressEntryCard(
    modifier: Modifier = Modifier,
    progressEntry: ProgressEntryWithImage,
    context: Context
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12),
        elevation = 0.dp,
        backgroundColor = CardColor
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = progressEntry.progressEntry.description ?: "",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = SimpleDateFormat("dd.MM.yyyy")
                        .format(progressEntry.progressEntry.timestamp),
                    fontSize = 12.sp,
                    color = LightTextColor
                )
            }
            Image(
                painter = rememberAsyncImagePainter(
                    imageNameToPath(context, progressEntry.image.filename)
                ),
                contentDescription = "progress image"
            )
        }
    }
}
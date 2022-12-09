package com.shellrider.minipainter.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.shellrider.minipainter.viewmodels.CreateEntryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shellrider.minipainter.ui.components.ButtonRow
import com.shellrider.minipainter.ui.components.OurRoundButton
import com.shellrider.minipainter.ui.components.TopBackground
import com.shellrider.minipainter.ui.theme.CardColor
import com.shellrider.minipainter.ui.theme.MainColor
import com.shellrider.minipainter.ui.theme.OnMainColor
import com.shellrider.minipainter.ui.theme.SecondaryColor

@Composable
fun CreateEntryHeading(text : String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = text,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.W700,
        fontSize = 32.sp,
        color = OnMainColor
    )
}

@Composable
fun CreateEntry(
    navController: NavController,
    imageLocation: String?,
    viewModel: CreateEntryViewModel = viewModel()
){
    val viewModel = viewModel
    val cachedImagePath by viewModel.cachedImagePath.observeAsState()
    val miniatureName by viewModel.miniatureName.observeAsState()

    if (imageLocation != null) {
        viewModel.setCachedImagePath(imageLocation)
    }
    TopBackground()
    Column(
        Modifier
            .padding(horizontal = 30.dp, vertical = 20.dp)
            .fillMaxSize()
    ) {
        if(cachedImagePath != null){
            CreateEntryHeading("Create Entry")
            Column(modifier = Modifier
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 24.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = CardColor)
                .weight(1f, true)

            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = rememberAsyncImagePainter(cachedImagePath),
                    contentDescription = "review of cropped image"
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp, bottom = 24.dp, top = 0.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp, vertical = 8.dp),
                        label = { Text(text = "Miniature Name") },
                        value = miniatureName?:"",
                        onValueChange = { viewModel.setMiniatureName(it) },
                        singleLine = true
                    )
                }
            }
            ButtonRow(){
                OurRoundButton(
                    text = "cancel",
                    color = SecondaryColor,
                    onClick = { navController.popBackStack() }
                )
                Log.d("CreateEntry", "Enabled: ${miniatureName != null}")
                OurRoundButton(
                    text = "save image",
                    enabled = miniatureName != null
                )
            }
        }
    }
}
package com.shellrider.minipainter.screens

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.shellrider.minipainter.filesystem.writeBitmapToStorage
import com.shellrider.minipainter.ui.components.ButtonRow
import com.shellrider.minipainter.ui.components.OurRoundButton
import com.shellrider.minipainter.ui.components.TopBackground
import com.shellrider.minipainter.ui.theme.CardColor
import com.shellrider.minipainter.ui.theme.SecondaryColor
import com.shellrider.minipainter.viewmodels.CreateEntryViewModel
import kotlinx.coroutines.runBlocking
import java.util.*

@Composable
fun CreateEntry(
    navController: NavController,
    imageLocation: String?,
) {
    val viewModel: CreateEntryViewModel? = LocalViewModelStoreOwner.current?.let {
        val viewModel: CreateEntryViewModel = viewModel(
            it,
            "CreateEntryViewModel",
            CreateEntryViewModelFactory(
                LocalContext.current.applicationContext
                        as Application
            )
        )
        viewModel
    }
    val context = LocalContext.current

    if (viewModel != null) {
        val cachedImagePath by viewModel.cachedImagePath.observeAsState()
        val miniatureName by viewModel.miniatureName.observeAsState()
        if (imageLocation != null) {
            viewModel.setCachedImagePath(imageLocation)
        }
        Column() {
            TopBackground("Create Entry")
            Column(
                Modifier
                    .padding(horizontal = 30.dp, vertical = 20.dp)
                    .fillMaxSize()
            ) {
                if (cachedImagePath != null) {
                    Column(
                        modifier = Modifier
                            .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 24.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(color = CardColor)
                            .weight(1f, true)

                    ) {
                        Log.d("Miniature", cachedImagePath ?: "")
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
                                value = miniatureName ?: "",
                                onValueChange = { viewModel.setMiniatureName(it) },
                                singleLine = true
                            )
                        }
                    }
                    ButtonRow() {
                        OurRoundButton(
                            text = "cancel",
                            color = SecondaryColor,
                            onClick = { navController.popBackStack() }
                        )
                        Log.d("CreateEntry", "Enabled: ${miniatureName != null}")
                        OurRoundButton(
                            text = "save image",
                            enabled = miniatureName != null,
                            onClick = {
                                val bitmap = BitmapFactory.decodeFile(cachedImagePath)
                                val filename = UUID.randomUUID().toString()
                                runBlocking {
                                    writeBitmapToStorage(context, bitmap, filename)
                                    viewModel.saveEntry(filename)
                                }
                                navController.navigate("home")
                            }
                        )
                    }
                }
            }
        }

    }
}

class CreateEntryViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateEntryViewModel(application) as T
    }
}

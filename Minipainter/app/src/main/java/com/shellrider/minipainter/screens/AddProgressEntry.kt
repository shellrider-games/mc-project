package com.shellrider.minipainter.screens

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shellrider.minipainter.filesystem.writeBitmapToStorage
import com.shellrider.minipainter.ui.components.AddImageEntryCard
import com.shellrider.minipainter.ui.components.ButtonRow
import com.shellrider.minipainter.ui.components.OurRoundButton
import com.shellrider.minipainter.ui.components.TopBackground
import com.shellrider.minipainter.ui.theme.SecondaryColor
import com.shellrider.minipainter.viewmodels.AddProgressEntryViewModel
import kotlinx.coroutines.runBlocking
import java.util.*

@Composable
fun AddProgressEntry(
    navController: NavController,
    imageLocation: String,
    miniatureId: Int
) {
    Log.d("AddProgressEntry", "ImageLocation: $imageLocation - miniatureId: $miniatureId")

    val viewModel: AddProgressEntryViewModel? = LocalViewModelStoreOwner.current?.let {
        val viewModel: AddProgressEntryViewModel = viewModel(
            it,
            "AddProgressEntryViewModel",
            AddProgressEntryViewModelFactory(
                LocalContext.current.applicationContext
                        as Application
            )
        )
        viewModel
    }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    if (viewModel == null) return

    val cachedImagePath by viewModel.cachedImagePath.observeAsState()
    viewModel.setCachedImagePath(imageLocation)
    val description by viewModel.description.observeAsState()


    Column(modifier = Modifier
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
    ) {
        TopBackground(text = "Record progress")
        if (cachedImagePath != null) {
            Column(
                Modifier
                    .padding(horizontal = 30.dp, vertical = 20.dp)
                    .fillMaxSize()
            ) {
                AddImageEntryCard(
                    modifier = Modifier.weight(1f),
                    imagePath = cachedImagePath!!
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp, vertical = 8.dp),
                        label = { Text(text = "Description") },
                        value = description ?: "",
                        onValueChange = { viewModel.setDescription(it) }
                    )
                }
                ButtonRow {
                    OurRoundButton(
                        text = "cancel",
                        color = SecondaryColor,
                        onClick = { navController.popBackStack() }
                    )
                    OurRoundButton(
                        text = "save image",
                        enabled = description != null,
                        onClick = {
                            val bitmap = BitmapFactory.decodeFile(cachedImagePath)
                            val filename = UUID.randomUUID().toString()
                            runBlocking {
                                writeBitmapToStorage(context, bitmap, filename)
                                viewModel.saveEntry(filename, miniatureId)
                            }
                            navController.popBackStack("home", false)
                            navController.navigate("miniature/$miniatureId")
                        }
                    )
                }
            }
        }
    }
}


class AddProgressEntryViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddProgressEntryViewModel(application) as T
    }
}
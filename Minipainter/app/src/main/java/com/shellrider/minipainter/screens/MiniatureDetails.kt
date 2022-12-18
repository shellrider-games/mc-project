package com.shellrider.minipainter.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.shellrider.minipainter.R
import com.shellrider.minipainter.filesystem.imageNameToPath
import com.shellrider.minipainter.ui.components.TopBackground
import com.shellrider.minipainter.ui.theme.CardColor
import com.shellrider.minipainter.ui.theme.LightTextColor
import com.shellrider.minipainter.ui.theme.MainColor
import com.shellrider.minipainter.ui.theme.OnMainColor
import com.shellrider.minipainter.viewmodels.MiniatureDetailsViewModel

@Composable
fun MiniatureDetails(
    navController: NavController,
    miniatureId: Int?
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val viewModel: MiniatureDetailsViewModel? = LocalViewModelStoreOwner.current?.let {
        val viewModel: MiniatureDetailsViewModel = viewModel(
            it,
            "MiniatureDetailsViewModel",
            miniatureId?.let { it1 ->
                MiniatureDetailsViewModelFactory(
                    LocalContext.current.applicationContext
                            as Application,
                    it1
                )
            },
        )
        viewModel
    }

    if (miniatureId == null) return
    if (viewModel?.miniature == null) return
    val miniature by viewModel.miniature.observeAsState()
    if (miniature == null) return

    //UI Cache
    var sliderPosition by rememberSaveable {
        mutableStateOf(viewModel.miniature.value?.miniature?.progress)
    }
    var modelDescriptionCache by rememberSaveable {
        mutableStateOf(viewModel.miniature.value?.miniature?.description)
    }

    var lazyColumnSize by remember { mutableStateOf(IntSize.Zero) }
    var density = LocalDensity.current.density
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        TopBackground(text = miniature?.miniature?.name) {
            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { showDeleteDialog = true },
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "delete miniature",
                    tint = OnMainColor
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    lazyColumnSize = it.size
                }

            ,
            contentPadding = PaddingValues(top = 0.dp, bottom = 32.dp),
        ) {
            item {
                Box() {
                    Image(
                        modifier = Modifier
                            .height((lazyColumnSize.width / density).toInt().dp)
                            .fillParentMaxWidth()
                            .padding(20.dp)
                            .clip(RoundedCornerShape(12.dp))
                        ,
                        painter = rememberAsyncImagePainter(miniature?.image?.filename?.let {
                            imageNameToPath(
                                context,
                                it
                            )
                        }), contentDescription = "Miniature image"
                    )
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                        ,
                        backgroundColor = MainColor,
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(id = R.drawable.add_a_photo),
                            contentDescription = "Add image to miniature",
                            tint = OnMainColor,
                        )
                    }
                }
            }
            item {
                Column(modifier = Modifier
                    .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        text = "Painting progress",
                        fontSize = 18.sp
                    )

                        sliderPosition?.let { it1 ->
                            Slider(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                value = it1,
                                onValueChange = {
                                    kotlin.run {
                                        sliderPosition = it
                                        viewModel.updateProgress(it)
                                    }
                                }
                            )
                        }

                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    backgroundColor = CardColor,
                    shape = RoundedCornerShape(10.dp)

                ) {
                    OutlinedTextField(
                        modifier = Modifier.padding(16.dp),
                        value = modelDescriptionCache ?:"",
                        label = {
                                Text(text = "Description")
                        },
                        onValueChange = {
                            modelDescriptionCache = it
                            viewModel.updateDescription(it)
                        },
                    )
                }
            }

        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                backgroundColor = CardColor,
                title = { Text(text = "Delete ${miniature?.miniature?.name}") },
                text = {
                    Text(
                        text = "This will permanently delete ${miniature?.miniature?.name}" +
                                " and all related data. Press delete to confirm"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        kotlin.run {
                            viewModel.deleteRequested()
                        }
                        navController.popBackStack("home", false)
                    }) {
                        Text("delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("cancel", color = LightTextColor)
                    }
                }
            )
        }
    }
}

class MiniatureDetailsViewModelFactory(
    val application: Application,
    val miniatureId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MiniatureDetailsViewModel(application, miniatureId) as T
    }
}
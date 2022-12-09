package com.shellrider.minipainter.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.shellrider.minipainter.filesystem.imageNameToPath
import com.shellrider.minipainter.ui.components.TopBackground
import com.shellrider.minipainter.ui.theme.OnMainColor
import com.shellrider.minipainter.viewmodels.MiniatureDetailsViewModel

@Composable
fun MiniatureDetails(
    navController: NavController,
    miniatureId: Int?
) {
    val context = LocalContext.current
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
    var lazyColumnSize by remember { mutableStateOf(IntSize.Zero) }
    var density = LocalDensity.current.density
    Column(Modifier.fillMaxSize()) {
        TopBackground(text = miniature?.miniature?.name) {
            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { /*TODO*/ },
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
                },
        ) {
                item {
                    Image(
                        modifier = Modifier
                            .height((lazyColumnSize.width / density).toInt().dp)
                            .fillParentMaxWidth()
                        ,
                        painter = rememberAsyncImagePainter(miniature?.image?.filename?.let {
                            imageNameToPath(
                                context,
                                it
                            )
                        }), contentDescription = "Miniature image"
                    )
                }

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
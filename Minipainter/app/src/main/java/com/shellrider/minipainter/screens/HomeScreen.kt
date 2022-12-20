package com.shellrider.minipainter.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shellrider.minipainter.ui.components.MiniatureOverviewCard
import com.shellrider.minipainter.ui.components.TopBackground
import com.shellrider.minipainter.ui.theme.MainColor
import com.shellrider.minipainter.ui.theme.OnMainColor
import com.shellrider.minipainter.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val miniaturesViewModel: HomeScreenViewModel? = LocalViewModelStoreOwner.current?.let {
        val viewModel: HomeScreenViewModel = viewModel(
            it,
            "MiniatureViewModel",
            HomeScreenViewModelFactory(
                LocalContext.current.applicationContext
                        as Application
            )
        )
        viewModel
    }
    Column {
        TopBackground("Miniatures")
        Box(modifier = Modifier.fillMaxSize()) {
            if (miniaturesViewModel != null) {
                val miniatures by miniaturesViewModel.miniatureWithLatestProgress.observeAsState(
                    listOf()
                )
                val context = LocalContext.current
                Column(
                    Modifier
                        .padding(horizontal = 30.dp, vertical = 0.dp)
                        .fillMaxSize()
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .weight(1f),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(top = 32.dp, bottom = 108.dp)
                    ) {
                        items(miniatures) {
                            val imageName by miniaturesViewModel.getImageFilenameFromId(it.progressEntry.imageId)
                                .observeAsState()
                            imageName?.let { it1 ->
                                MiniatureOverviewCard(
                                    context = context,
                                    miniature = it,
                                    imageName = it1.filename,
                                    onClick = {
                                        navController.navigate("miniature/${it.miniature.id}")
                                    }
                                )
                            }
                        }
                    }

                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                backgroundColor = MainColor,
                onClick = { navController.navigate("camera") }
            ) {
                Icon(
                    Icons.Default.Add,
                    modifier = Modifier.size(27.dp),
                    contentDescription = "take new image",
                    tint = OnMainColor
                )
            }


        }

    }
}

class HomeScreenViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(application) as T
    }
}
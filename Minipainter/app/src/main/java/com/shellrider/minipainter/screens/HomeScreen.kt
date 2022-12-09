package com.shellrider.minipainter.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shellrider.minipainter.R
import com.shellrider.minipainter.ui.components.BottomNavigationBar
import com.shellrider.minipainter.ui.components.MiniatureOverviewCard
import com.shellrider.minipainter.ui.components.TopBackground
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
    Column() {
        TopBackground("Miniatures")
        Box(modifier = Modifier.fillMaxSize()) {
            if (miniaturesViewModel != null) {
                val miniatures by miniaturesViewModel.miniaturesWithPrimaryImage.observeAsState(
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
                            MiniatureOverviewCard(
                                context = context,
                                miniature = it,
                                onClick = {
                                    navController.navigate("miniature/${it.miniature.id}")
                                }
                            )
                        }
                    }

                }
            }
            BottomNavigationBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { navController.navigate("camera") }) {
                    Icon(
                        modifier = Modifier.size(27.dp),
                        painter = painterResource(id = R.drawable.photo_camera),
                        contentDescription = "take new image",
                        tint = OnMainColor
                    )
                }

            }
        }

    }
}

class HomeScreenViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(application) as T
    }
}
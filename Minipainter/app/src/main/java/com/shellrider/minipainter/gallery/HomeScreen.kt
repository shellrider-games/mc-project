package com.shellrider.minipainter.gallery

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shellrider.minipainter.R
import com.shellrider.minipainter.datamodel.Miniature
import com.shellrider.minipainter.ui.components.BottomNavigationBar
import com.shellrider.minipainter.ui.theme.IconsOnNavigationColor
import com.shellrider.minipainter.ui.theme.MainColor
import com.shellrider.minipainter.ui.theme.OnMainColor
import com.shellrider.minipainter.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val miniaturesViewModel:HomeScreenViewModel? = LocalViewModelStoreOwner.current?.let {
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
    Box(modifier = Modifier.fillMaxSize()) {
        if (miniaturesViewModel != null) {
            val miniatures by miniaturesViewModel.miniaturesWithPrimaryImage.observeAsState(listOf())
            LazyColumn {
                items(miniatures) {
                    Text(text = it.miniature.name)
                }
            }
        }

        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            IconButton(onClick = { navController.navigate("camera")}) {
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

class  HomeScreenViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(application) as T
    }
}
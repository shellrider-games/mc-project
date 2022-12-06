package com.shellrider.minipainter.gallery

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shellrider.minipainter.datamodel.Miniature
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
    if(miniaturesViewModel != null) {
        val miniatures by miniaturesViewModel.miniatures.observeAsState(listOf())
        LazyColumn() {
            items(miniatures) {
                Text(text = it.name)
            }
        }
    }
}

class  HomeScreenViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(application) as T
    }
}
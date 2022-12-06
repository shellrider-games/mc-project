package com.shellrider.minipainter

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shellrider.minipainter.camera.CameraCapture
import com.shellrider.minipainter.camera.CameraPermission
import com.shellrider.minipainter.capture.MiniaturePictureCapture
import com.shellrider.minipainter.datamodel.MiniatureRoomDatabase
import com.shellrider.minipainter.filesystem.isExternalStorageWritable
import com.shellrider.minipainter.filesystem.writeImageToStorage
import com.shellrider.minipainter.gallery.HomeScreen
import com.shellrider.minipainter.ui.theme.MinipainterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinipainterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                        MinipainterApp()
                    }
                }
            }
        }
    }


@Composable
fun MinipainterApp() {
    val navController = rememberNavController()
    Box(modifier = Modifier) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(navController)
            }
            composable("miniatureCamera") {
                MiniaturePictureCapture(navController)
            }
        }
    }
}




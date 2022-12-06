@file:OptIn(ExperimentalPermissionsApi::class)

package com.shellrider.material3camera

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.UseCase
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.*
import com.shellrider.material3camera.ui.theme.Material3cameraTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Material3cameraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeContent()
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent() {
    MiniatureApp()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniatureApp(){
    val navController = rememberNavController()

    Scaffold() {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(onCameraButtonPressed = { navController.navigate("camera") })
            }
            composable("camera") {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val emptyImageUri = Uri.parse("file://dev/null")
    var imageUri by remember { mutableStateOf(emptyImageUri) }
    if (imageUri != emptyImageUri) {
        Box(modifier = modifier) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberImagePainter(imageUri),
                contentDescription = "Captured image"
            )
            Button(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = {
                    imageUri = emptyImageUri
                }
            ) {
                Text("Remove image")
            }
        }
    } else {
        CameraScreen()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onCameraButtonPressed: () -> Unit){
    Scaffold(
        floatingActionButton = { CameraButton(onCameraButtonPressed) }
    ) {

    }
}

@Composable
fun CameraScreen(){
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if(event == Lifecycle.Event.ON_START) {
                    permissionState.launchPermissionRequest()
                }}
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
    })

    if(permissionState.status.isGranted) {
        Surface {
            CameraCapture()
        }
    } else {
        CameraPermissionMissing(permissionState = permissionState)
    }
}

@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (File) -> Unit = { }
){
    Box(modifier = modifier) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        var previewUseCase by remember { mutableStateOf<UseCase>(androidx.camera.core.Preview.Builder().build()) }
        val imageCaptureUseCase by remember {
            mutableStateOf(
                ImageCapture.Builder()
                    .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
            )
        }

        val coroutineScope = rememberCoroutineScope()

        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onUseCase = {
                previewUseCase = it
            }
        )
        LaunchedEffect(previewUseCase) {
            val cameraProvider = context.getCameraProvider()
            try {
                // Must unbind the use-cases before rebinding them.
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
                )
            } catch (ex: Exception) {
                Log.e("CameraCapture", "Failed to bind camera use cases", ex)
            }
        }
        Box {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onUseCase = {
                    previewUseCase = it
                }
            )
            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        onImageFile(imageCaptureUseCase.takePicture(context.executor))
                    }
                }
            ) {
                Text("Take Photo!")
            }
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    onUseCase: (UseCase) -> Unit = { }
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            onUseCase(androidx.camera.core.Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
            )
            previewView
    })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionMissing(permissionState: PermissionState){
    var cameraPermissionMessage: String = ""
    when {
        permissionState.status.shouldShowRationale -> {
            cameraPermissionMessage = "Please allow camera permissions, this is a camera app!"
        }
        (!permissionState.status.shouldShowRationale && !permissionState.status.isGranted) -> {
            cameraPermissionMessage =
                "Camera permission permanently denied, you can enable it in the app settings."
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) { Text(cameraPermissionMessage) }
}

@Composable
fun CameraButton(onCameraButtonPressed: () -> Unit) {
    FloatingActionButton(onClick = { onCameraButtonPressed() }) {
        Icon(Icons.Outlined.Add, "")
    }
}

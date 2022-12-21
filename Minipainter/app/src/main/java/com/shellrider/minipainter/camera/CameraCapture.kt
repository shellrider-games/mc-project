package com.shellrider.minipainter.camera

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.view.PreviewView.ScaleType
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.shellrider.minipainter.ui.components.OurRoundButton
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (File) -> Unit = { },
    previewScaleType: ScaleType = ScaleType.FIT_CENTER,
    cameraOverlay: @Composable () -> Unit = { }
) {
    var buttonClickable by remember { mutableStateOf(true) }
    Box(modifier = modifier) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        var previewUseCase by remember {
            mutableStateOf<UseCase>(Preview.Builder().build())
        }
        val imageCaptureUseCase by remember {
            mutableStateOf(
                ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
            )
        }

        val coroutineScope = rememberCoroutineScope()

        Box {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onUseCase = {
                    previewUseCase = it
                },
                scaleType = previewScaleType
            )

            cameraOverlay()

            OurRoundButton(
                modifier = Modifier
                    .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 24.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                    if (buttonClickable) {
                        coroutineScope.launch {
                            onImageFile(imageCaptureUseCase.takePicture(context.executor))
                        }
                        buttonClickable = false
                    }
                },
                text = "save image"
            )
        }
        LaunchedEffect(previewUseCase) {
            val cameraProvider = context.getCameraProvider()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
                )
            } catch (ex: Exception) {
                Log.e("CameraCapture", "Failed to bind camera use cases", ex)
            }
        }
    }
}
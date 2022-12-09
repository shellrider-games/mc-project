package com.shellrider.minipainter.camera

import android.util.Log
import android.util.Rational
import androidx.camera.core.AspectRatio
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.view.PreviewView
import androidx.camera.view.PreviewView.ScaleType
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shellrider.minipainter.ui.components.OurRoundButton
import com.shellrider.minipainter.ui.theme.MainColor
import com.shellrider.minipainter.ui.theme.OnMainColor
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (File) -> Unit = { },
    previewScaleType: ScaleType = PreviewView.ScaleType.FIT_CENTER,
    cameraOverlay: @Composable () -> Unit = { }
) {
    var buttonClickable by remember { mutableStateOf(true) }
    Box(modifier = modifier){
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
                    .align(Alignment.BottomCenter)
                ,
                onClick = {
                        if(buttonClickable) {
                            coroutineScope.launch {
                                imageCaptureUseCase.takePicture(context.executor)
                                    .let { onImageFile(it) }
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
package com.shellrider.minipainter.capture

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavController
import com.shellrider.minipainter.camera.CameraCapture
import com.shellrider.minipainter.camera.CameraPermission
import com.shellrider.minipainter.capture.overlays.CropBorderOverlay
import com.shellrider.minipainter.extensions.rotateAccordingToExifInterface
import com.shellrider.minipainter.filesystem.writeBitmapToCache
import kotlin.math.roundToInt


@Composable
fun FullViewWithCropBorder(navController: NavController) {
    val context = LocalContext.current
    var layoutHeight by remember { mutableStateOf(0) }
    var layoutWidth by remember { mutableStateOf(0) }
    var localDensity = LocalDensity.current.density

    CameraPermission {
        CameraCapture(
            modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                layoutHeight = layoutCoordinates.size.height
                layoutWidth = layoutCoordinates.size.width
            },
            previewScaleType = PreviewView.ScaleType.FILL_CENTER,
            cameraOverlay = { CropBorderOverlay() },
            onImageFile = { file ->

                val exifInterface = ExifInterface(file.path)
                var bitmap = BitmapFactory.decodeFile(file.path)
                bitmap = bitmap.rotateAccordingToExifInterface(exifInterface)
                Log.d(
                    "TakePicture",
                    "Bitmap size after flip - Width: ${bitmap.width}" +
                            " Height: ${bitmap.height}"
                )
                var cropWidth = (320 * localDensity * bitmap.height / layoutHeight).roundToInt()
                bitmap = Bitmap.createBitmap(
                    bitmap,
                    (bitmap.width - cropWidth) / 2,
                    (bitmap.height - cropWidth) / 2,
                    cropWidth,
                    cropWidth
                )
                val imageFilePath = writeBitmapToCache(context, bitmap)
                Log.d("TakePicture", "Cached file at: $imageFilePath")
                navController.navigate("create_entry/${Uri.encode(imageFilePath)}")
            }
        )
    }
}
package com.shellrider.minipainter.capture

import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
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
import com.shellrider.minipainter.extensions.cropFromCenter
import com.shellrider.minipainter.extensions.rotateAccordingToExifInterface
import com.shellrider.minipainter.filesystem.writeBitmapToCache
import kotlin.math.roundToInt

@Composable
fun NewProgressEntryCapture(navController: NavController, miniatureId: Int) {
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

                var cropWidth = (320 * localDensity * bitmap.height / Integer.max(
                    layoutHeight,
                    layoutWidth
                )).roundToInt()
                bitmap = bitmap.cropFromCenter(cropWidth)

                val imageFilePath = writeBitmapToCache(context, bitmap)
                navController.navigate("add_progress_entry/$miniatureId/${Uri.encode(imageFilePath)}")
            }
        )
    }
}
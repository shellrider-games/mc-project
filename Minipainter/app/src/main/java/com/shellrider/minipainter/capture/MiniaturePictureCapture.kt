package com.shellrider.minipainter.capture

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.shellrider.minipainter.camera.CameraCapture
import com.shellrider.minipainter.camera.CameraPermission
import com.shellrider.minipainter.datamodel.Image
import com.shellrider.minipainter.datamodel.MiniatureRoomDatabase
import com.shellrider.minipainter.filesystem.writeImageToStorage
import kotlinx.coroutines.launch

@Composable
fun MiniaturePictureCapture(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    CameraPermission {
        CameraCapture(
            onImageFile = { file ->
                if(file != null) {
                    writeImageToStorage(context, file)
                    coroutineScope.launch { MiniatureRoomDatabase.getDatabase(context).imageDao().insert(
                        Image(id = 0, filename = file.name)
                    ) }
                    Toast.makeText(context, "Took picture and saved it to storage!", Toast.LENGTH_SHORT).show()
                    navController.navigate("home")
                }
            },
        )
    }
}
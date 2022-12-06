package com.shellrider.minipainter.camera

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(
    permission: String = android.Manifest.permission.CAMERA,
    rationale: String = "This permission is necessary to use the camera features of this app. Please grant the permission.",
    noPermissionText: String = "This application needs camera permission if you want to take pictures, please enable them in the settings.",
    content: @Composable () -> Unit = { }
) {
    val permissionState = rememberPermissionState(permission = permission)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionState.launchPermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        })

    if (permissionState.status.isGranted) {
        content()
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (permissionState.status.shouldShowRationale) {
                Text(text = rationale)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text(text = "Request permission")
                }
            } else {
                Text(text = noPermissionText)
            }
        }
    }
}
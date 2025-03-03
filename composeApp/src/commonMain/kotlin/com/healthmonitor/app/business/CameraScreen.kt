package com.healthmonitor.app.business

import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.healthmonitor.app.business.facecamera.CameraAnalyzer
import com.healthmonitor.app.utils.rememberCameraPermissionState
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    var cameraLens by remember { mutableStateOf(CameraSelector.LENS_FACING_FRONT) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var healthReport by remember { mutableStateOf("Scanning...\n\nPlease look straight at the camera.") }

    val hasPermission = rememberCameraPermissionState()
    if (!hasPermission) {
        Text("Camera permission is required to use this feature.")
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }.also { previewView = it }
            },
            modifier = Modifier.weight(1f)
        )

        LaunchedEffect(cameraLens) {
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                bindCamera(cameraProvider, lifecycleOwner, previewView, cameraLens) { result ->
                    healthReport = result
                }
            }, ContextCompat.getMainExecutor(context))
        }

        // Health report section
        SelectionContainer {
            Text(
                text = healthReport,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Button to switch camera
        Button(
            onClick = { cameraLens = if (cameraLens == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(text = "Switch Camera")
        }
    }
}

private fun bindCamera(
    cameraProvider: ProcessCameraProvider,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView?,
    lensFacing: Int,
    onFaceDetected: (String) -> Unit
) {
    try {
        if (previewView == null) return
        cameraProvider.unbindAll()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(previewView.context), CameraAnalyzer(onFaceDetected))

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
    } catch (exc: Exception) {
        // Log error
    }
}

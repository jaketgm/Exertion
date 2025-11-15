package com.example.exertion.utils.camera

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

@Composable
fun InlineCameraPreview(
    modifier: Modifier = Modifier,
    onDetectionUpdated: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Controller lives across recompositions
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_ANALYSIS
            )
        }
    }

    LaunchedEffect(cameraController) {
        // Create and attach your real analyzer here
        val analyzer = ObjectRecognitionAnalyzer(context) { detected ->
            onDetectionUpdated(detected)
        }

        cameraController.setImageAnalysisAnalyzer(
            Executors.newSingleThreadExecutor(),
            analyzer
        )

        cameraController.bindToLifecycle(lifecycleOwner)
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
                controller = cameraController
            }
        },
        update = { view ->
            view.controller = cameraController
        }
    )
}
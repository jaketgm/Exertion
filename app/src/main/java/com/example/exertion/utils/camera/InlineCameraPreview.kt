package com.example.exertion.utils.camera

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

@Composable
fun InlineCameraPreview(
    modifier: Modifier = Modifier,
    onDetectionUpdated: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Log.d("EC-PREVIEW", "InlineCameraPreview COMPOSED")

    val preview = remember {
        Preview.Builder().build().also {
            Log.d("EC-PREVIEW", "Preview use case BUILT")
        }
    }

    val analysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { Log.d("EC-PREVIEW", "Analysis use case BUILT") }
    }

    val cameraProviderFuture = remember {
        Log.d("EC-PREVIEW", "cameraProviderFuture REQUESTED")
        ProcessCameraProvider.getInstance(context)
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER

                Log.d("EC-PREVIEW", "PreviewView FACTORY created")

                cameraProviderFuture.addListener({
                    Log.d("EC-PREVIEW", "cameraProviderFuture LISTENER TRIGGERED")

                    val cameraProvider = cameraProviderFuture.get()

                    try {
                        cameraProvider.unbindAll()
                        Log.d("EC-PREVIEW", "CameraProvider unbindAll done")

                        preview.setSurfaceProvider(surfaceProvider)
                        Log.d("EC-PREVIEW", "SurfaceProvider SET")

                        val analyzer = ObjectRecognitionAnalyzer(ctx) {
                            Log.d("EC-PREVIEW", "Analyzer detected object: $it")
                            onDetectionUpdated(it)
                        }
                        analysis.setAnalyzer(
                            Executors.newSingleThreadExecutor(),
                            analyzer
                        )

                        Log.d("EC-PREVIEW", "Analyzer attached")

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            analysis
                        )

                        Log.d("EC-PREVIEW", "Camera BOUND and RUNNING")

                    } catch (exc: Exception) {
                        Log.e("EC-PREVIEW", "Camera binding FAILED", exc)
                    }

                }, ContextCompat.getMainExecutor(ctx))
            }
        }
    )
}
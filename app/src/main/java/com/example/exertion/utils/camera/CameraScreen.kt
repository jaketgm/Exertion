package com.example.exertion.utils.camera

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.internal.SynchronizedObject
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_ANALYSIS or LifecycleCameraController.VIDEO_CAPTURE or LifecycleCameraController.IMAGE_CAPTURE
            )
        }
    }

    var detectedObject by remember {
        mutableStateOf("No Object Detected")
    }

    var analyzer by remember {
        mutableStateOf<ObjectRecognitionAnalyzer?>(null)
    }

    LaunchedEffect(Unit) {
        try {
            Log.d("CameraScreen", "Creating analyzer...")
            val analyzer = ObjectRecognitionAnalyzer(context) { updated ->
                detectedObject = updated
            }
            Log.d("CameraScreen", "Setting analyzer on cameraController...")
            cameraController.setImageAnalysisAnalyzer(
                Executors.newSingleThreadExecutor(),
                analyzer
            )
            Log.d("CameraScreen", "Analyzer successfully attached.")
        } catch (e: Exception) {
            Log.e("CameraScreen", "Error initializing analyzer", e)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Gym Object Scanner") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                },
                update = { it.controller = cameraController }
            )

            // Overlay text
            Text(
                text = detectedObject,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CameraContent() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Controller lives across recompositions
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            // Enable the use cases you need
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_ANALYSIS or
                        LifecycleCameraController.VIDEO_CAPTURE or
                        LifecycleCameraController.IMAGE_CAPTURE
            )
        }
    }

    var detectedObject by remember { mutableStateOf("No object detected") }

    fun onObjectUpdated(updated: String) { detectedObject = updated }

    // Example analyzer hook (replace with real detector later)
    val analyzer = remember {
        androidx.camera.core.ImageAnalysis.Analyzer { imageProxy ->
            onObjectUpdated(detectedObject)
        }
    }

    // Attach analyzer (executor is required)
    LaunchedEffect(Unit) {
        cameraController.setImageAnalysisAnalyzer(
            java.util.concurrent.Executors.newSingleThreadExecutor(),
            analyzer
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Object Scanner") }) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx: Context ->
                    PreviewView(ctx).apply {
                        setBackgroundColor(android.graphics.Color.BLACK) // expects @ColorInt
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START

                        controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                },
                update = { previewView: PreviewView ->
                    previewView.controller = cameraController
                }
            )

            // Simple overlay label
            Text(
                text = detectedObject,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f))
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun startObjectRecognition(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedObjectUpdated: (String) -> Unit
) {

}

/*
* For testing, since I obviously cannot bring my computer to the gym, and my android cameraX is
* connected to my webcam. I am going to create a new kt file, which will feed an inputted video
* (like in assets), and then runs the analyzer on that. How can I do this?
* */
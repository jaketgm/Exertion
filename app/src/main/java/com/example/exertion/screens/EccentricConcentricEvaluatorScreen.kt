package com.example.exertion.screens

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.media3.datasource.AssetDataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.exertion.ui_components.buttons.EccentricConcentricButton
import com.example.exertion.ui_components.navbar.NavBar
import com.example.exertion.ui_components.topbar.StableTopBar
import com.example.exertion.utils.camera.InlineCameraPreview
import com.example.exertion.utils.camera.ObjectRecognitionAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EccentricConcentricEvaluatorScreen(
    navController: NavController? = null,
    exerciseName: String = "Bench Press"
) {
    var isDetecting by remember { mutableStateOf(false) }
    var detectionText by remember { mutableStateOf("Exercise: $exerciseName") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        // Navbar with back button
        NavBar(
            user_name = "",
            is_dark_mode = true,
            show_back_button = true,
            nav_controller = navController,
            on_profile_click = {}
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .background(Color(0xFF000000))
        ) {
            // Camera preview as background when detecting
            if (isDetecting) {
                InlineCameraPreview(
                    modifier = Modifier.matchParentSize(),
                    onDetectionUpdated = { detected ->
                        detectionText = detected
                    }
                )
            }

            // Top text bar
            Text(
                text = detectionText,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )

            // Detect button (only when not detecting)
            if (!isDetecting) {
                EccentricConcentricButton(
                    text = "Detect",
                    size = 160f,
                    onClick = {
                        isDetecting = true
                        detectionText = "Detecting..."
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    name = "Eccentric Concentric – Preview"
)
@Composable
fun PreviewEccentricConcentricEvaluatorScreen() {
    var isDetecting by remember { mutableStateOf(false) }
    var detectionText by remember { mutableStateOf("Exercise: Bench Press") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavBar(
            user_name = "Jake",
            is_dark_mode = true,
            show_back_button = true,
            nav_controller = null,
            on_profile_click = {}
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(Color(0xFF0A0A0A))
                .padding(12.dp)
        ) {
            Text(
                text = detectionText,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (!isDetecting) {
                EccentricConcentricButton(
                    text = "Detect",
                    size = 160f,
                    onClick = { isDetecting = true },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Text(
                    text = "Detecting… (Preview Mode)",
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 18.sp
                )
            }
        }
    }
}


//@OptIn(UnstableApi::class)
//@Composable
//fun EccentricConcentricEvaluatorScreen(context: Context = LocalContext.current) {
//    val labelText = remember { mutableStateOf("Loading...") }
//    val scope = rememberCoroutineScope()
//
//    // 1. Initialize ExoPlayer
//    val player = remember {
//        val p = ExoPlayer.Builder(context).build()
//        try {
//            val dataSpec = DataSpec.Builder()
//                .setUri(Uri.parse("asset:///vid4.mp4"))
//                .build()
//            val assetDataSource = AssetDataSource(context)
//            assetDataSource.open(dataSpec)
//
//            val factory = DataSource.Factory { assetDataSource }
//            val mediaItem = MediaItem.Builder()
//                .setUri("asset:///vid4.mp4")
//                .build()
//            val mediaSource = ProgressiveMediaSource.Factory(factory)
//                .createMediaSource(mediaItem)
//
//            p.setMediaSource(mediaSource)
//            p.prepare()
//            p.playWhenReady = true
//            Log.d("VideoDetectionScreen", "Video prepared and playing")
//        } catch (e: Exception) {
//            Log.e("VideoDetectionScreen", "Error loading video", e)
//        }
//        p
//    }
//
//    DisposableEffect(Unit) {
//        onDispose { player.release() }
//    }
//
//    // 2. Initialize analyzer
//    val analyzer = remember {
//        ObjectRecognitionAnalyzer(context) { detected ->
//            labelText.value = detected
//            Log.d("VideoDetectionScreen", "Detection result: $detected")
//        }
//    }
//
//    // 3. Background frame extraction
//    LaunchedEffect(Unit) {
//        scope.launch(Dispatchers.IO) {
//            try {
//                Log.d("VideoDetectionScreen", "Starting frame extraction...")
//                val retriever = MediaMetadataRetriever()
//                val descriptor = context.assets.openFd("vid1.mp4")
//                retriever.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
//
//                val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
//                Log.d("VideoDetectionScreen", "Video duration: ${durationMs}ms")
//
//                var timeUs = 0L
//                val frameIntervalUs = 500_000L // every 0.5s
//
//                while (timeUs < durationMs * 1000) {
//                    val frameBitmap = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST)
//                    if (frameBitmap != null) {
//                        Log.d("VideoDetectionScreen", "Processing frame at ${timeUs / 1_000_000.0}s")
//                        val result = analyzer.detectObjects(frameBitmap)
//                        withContext(Dispatchers.Main) {
//                            labelText.value = result
//                        }
//                    } else {
//                        Log.w("VideoDetectionScreen", "Null frame at ${timeUs / 1_000_000.0}s")
//                    }
//                    timeUs += frameIntervalUs
//                }
//
//                retriever.release()
//                Log.d("VideoDetectionScreen", "Finished frame extraction.")
//            } catch (e: Exception) {
//                Log.e("VideoDetectionScreen", "Error during frame analysis", e)
//            }
//        }
//    }
//
//    // 4. UI
//    Scaffold(
//        topBar = { StableTopBar("Gym Object Video Demo") }
//    ) { padding ->
//        Column(
//            Modifier
//                .fillMaxSize()
//                .padding(padding)
//        ) {
//            AndroidView(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(16 / 9f),
//                factory = { ctx ->
//                    PlayerView(ctx).apply { this.player = player }
//                }
//            )
//
//            Text(
//                text = labelText.value,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.White.copy(alpha = 0.8f))
//                    .padding(16.dp),
//                textAlign = TextAlign.Center,
//                style = MaterialTheme.typography.bodyLarge
//            )
//        }
//    }
//}

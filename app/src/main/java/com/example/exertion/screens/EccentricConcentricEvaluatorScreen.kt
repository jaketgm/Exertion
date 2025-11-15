package com.example.exertion.screens

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.media3.datasource.AssetDataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(UnstableApi::class)
@Composable
fun EccentricConcentricEvaluatorScreen(context: Context = LocalContext.current) {
    val labelText = remember { mutableStateOf("Loading...") }
    val scope = rememberCoroutineScope()

    // 1. Initialize ExoPlayer
    val player = remember {
        val p = ExoPlayer.Builder(context).build()
        try {
            val dataSpec = DataSpec.Builder()
                .setUri(Uri.parse("asset:///vid4.mp4"))
                .build()
            val assetDataSource = AssetDataSource(context)
            assetDataSource.open(dataSpec)

            val factory = DataSource.Factory { assetDataSource }
            val mediaItem = MediaItem.Builder()
                .setUri("asset:///vid4.mp4")
                .build()
            val mediaSource = ProgressiveMediaSource.Factory(factory)
                .createMediaSource(mediaItem)

            p.setMediaSource(mediaSource)
            p.prepare()
            p.playWhenReady = true
            Log.d("VideoDetectionScreen", "Video prepared and playing")
        } catch (e: Exception) {
            Log.e("VideoDetectionScreen", "Error loading video", e)
        }
        p
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    // 2. Initialize analyzer
    val analyzer = remember {
        ObjectRecognitionAnalyzer(context) { detected ->
            labelText.value = detected
            Log.d("VideoDetectionScreen", "Detection result: $detected")
        }
    }

    // 3. Background frame extraction
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                Log.d("VideoDetectionScreen", "Starting frame extraction...")
                val retriever = MediaMetadataRetriever()
                val descriptor = context.assets.openFd("vid1.mp4")
                retriever.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)

                val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
                Log.d("VideoDetectionScreen", "Video duration: ${durationMs}ms")

                var timeUs = 0L
                val frameIntervalUs = 500_000L // every 0.5s

                while (timeUs < durationMs * 1000) {
                    val frameBitmap = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST)
                    if (frameBitmap != null) {
                        Log.d("VideoDetectionScreen", "Processing frame at ${timeUs / 1_000_000.0}s")
                        val result = analyzer.detectObjects(frameBitmap)
                        withContext(Dispatchers.Main) {
                            labelText.value = result
                        }
                    } else {
                        Log.w("VideoDetectionScreen", "Null frame at ${timeUs / 1_000_000.0}s")
                    }
                    timeUs += frameIntervalUs
                }

                retriever.release()
                Log.d("VideoDetectionScreen", "Finished frame extraction.")
            } catch (e: Exception) {
                Log.e("VideoDetectionScreen", "Error during frame analysis", e)
            }
        }
    }

    // 4. UI
    Scaffold(
        topBar = { StableTopBar("Gym Object Video Demo") }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
                factory = { ctx ->
                    PlayerView(ctx).apply { this.player = player }
                }
            )

            Text(
                text = labelText.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

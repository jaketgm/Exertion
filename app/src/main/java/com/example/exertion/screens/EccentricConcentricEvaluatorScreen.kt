package com.example.exertion.screens

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.exertion.R
import com.example.exertion.data.datastore.UserPreferencesDataStore
import com.example.exertion.data.ec.ECFrameMetrics
import com.example.exertion.data.ec.ECRepData
import com.example.exertion.data.ec.ECRepDetector
import com.example.exertion.data.ec.ECSetData
import com.example.exertion.data.ec.saveECSetToDB
import com.example.exertion.data.exercise.ExerciseTable
import com.example.exertion.data.exercise.ExerciseVM
import com.example.exertion.data.rep_entry.RepEntryVM
import com.example.exertion.data.set_entry.SetEntryVM
import com.example.exertion.ui_components.buttons.EccentricConcentricButton
import com.example.exertion.ui_components.navbar.NavBar
import com.example.exertion.utils.camera.InlineCameraPreview
import com.example.exertion.utils.camera.ObjectRecognitionAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EccentricConcentricEvaluatorScreen(
    navController: NavController? = null,
    exerciseName: String = "Bench Press"
) {
    var isDetecting by remember { mutableStateOf(false) }
    var detectionText by remember { mutableStateOf("Exercise: $exerciseName") }
    var useStaticAnalysis by remember { mutableStateOf(false) }

    val setVM: SetEntryVM = viewModel()
    val repVM: RepEntryVM = viewModel()
    val exerciseVM: ExerciseVM = viewModel()
    val exercises by exerciseVM.allExercises.collectAsState(initial = emptyList())
    var showExerciseMenu by remember { mutableStateOf(false) }

    val staticVideos = listOf(
        "Bench Press" to R.raw.bench,
        "Squat" to R.raw.back_squat,
        "Chin Ups" to R.raw.chin_ups,
        "Overhead Press" to R.raw.overhead_press,
        "Tricep Extensions" to R.raw.tricep_extensions
    )

    var selectedStaticVideoRes by remember { mutableStateOf(R.raw.bench) }

    // live rep tracking
    var reps by remember { mutableStateOf(emptyList<ECRepData>()) }
    val repDetector = remember { ECRepDetector() }

    val scope = rememberCoroutineScope()

    val currentSet = ECSetData(
        workoutExerciseId = 1,
        weightKg = 60.0,
        setIndex = 1,
        reps = reps,
        rir = null,
        rpe = null
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        val context = LocalContext.current
        val prefs = UserPreferencesDataStore(context)
        val userId by prefs.userIdFlow.collectAsState(initial = null)

        NavBar(
            user_name = if (userId != null) "Jake" else "Guest",
            is_dark_mode = true,
            show_back_button = true,
            nav_controller = navController,
            loggedInUserId = userId,
            on_profile_click = {
                // not logged in
                navController?.navigate("login")
            },
            on_settings_click = {
                // logged in
                navController?.navigate("settings/$userId")
            }
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .background(Color(0xFF000000))
        ) {
            if (isDetecting) {
                if (useStaticAnalysis) {
                    ECStaticVideoInlinePlayer(
                        modifier = Modifier.matchParentSize(),
                        videoResId = selectedStaticVideoRes,
                        onLabelUpdated = { detectionText = it },
                        onFrameMetrics = { metrics ->
                            val rep = repDetector.onSample(metrics)
                            if (rep != null) reps = reps + rep
                        }
                    )
                } else {
                    InlineCameraPreview(
                        modifier = Modifier.matchParentSize(),
                        onLabelUpdated = { detectionText = it },
                        onFrameMetrics = { metrics ->
                            val rep = repDetector.onSample(metrics)
                            if (rep != null) reps = reps + rep
                        }
                    )
                }
            }

            Text(
                text = detectionText,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )

            if (!isDetecting) {
                EccentricConcentricButton(
                    text = "Detect",
                    size = 160f,
                    onClick = {
                        isDetecting = true
                        detectionText = "Detecting..."

                        useStaticAnalysis = true

                        // For dynamic camera, comment out static and switch this to false:
                        // useStaticAnalysis = false
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        ECActionBar(
            onSaveData = {
                scope.launch {
                    saveECSetToDB(
                        setVM = setVM,
                        repVM = repVM,
                        data = currentSet
                    )
                }
            },
            onSelectExercise = {
                showExerciseMenu = true
            }
        )

        if (showExerciseMenu) {
            if (useStaticAnalysis) {
                StaticVideoDropdownMenu(
                    videos = staticVideos,
                    onSelect = { pair ->
                        detectionText = "Exercise: ${pair.first}"
                        selectedStaticVideoRes = pair.second

                        isDetecting = false
                        reps = emptyList()

                        showExerciseMenu = false
                    },
                    onDismiss = { showExerciseMenu = false }
                )
            } else {
                ExerciseDropdownMenu(
                    exercises = exercises,
                    onSelect = { selected ->
                        detectionText = "Exercise: ${selected.name}"
                        showExerciseMenu = false
                    },
                    onDismiss = { showExerciseMenu = false }
                )
            }
        }

        LaunchedEffect(selectedStaticVideoRes) {
            if (useStaticAnalysis && isDetecting) {
                isDetecting = true
            }
        }

        if (showExerciseMenu) {
            ExerciseDropdownMenu(
                exercises = exercises,
                onSelect = { selected ->
                    detectionText = "Exercise: ${selected.name}"
                    showExerciseMenu = false
                    // TODO: also update workoutExerciseId & weightKg
                },
                onDismiss = { showExerciseMenu = false }
            )
        }

        Spacer(Modifier.height(20.dp))

        if (reps.isNotEmpty()) {
            ECStatsContainer(
                reps = reps,
                setSummary = currentSet
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp)
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No reps detected yet. Start moving through full ROM.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun ECActionBar(
    onSaveData: () -> Unit,
    onSelectExercise: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000000),
                        Color(0xFF000000)
                    )
                )
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(42.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x33FFFFFF))
                    .clickable { onSaveData() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Save Data",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .height(42.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x33FFFFFF))
                    .clickable { onSelectExercise() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Select Another Exercise",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_down_arrow),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseDropdownMenu(
    exercises: List<ExerciseTable>,
    onSelect: (ExerciseTable) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable(onClick = { onDismiss() }, indication = null, interactionSource = remember { MutableInteractionSource() })
    ) {

        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
                .height(380.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 1f),
                            Color.Black.copy(alpha = 0.85f),
                            Color.Black.copy(alpha = 1f)
                        )
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
            ) {
                items(exercises) { exercise ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .background(Color(0xFF101010))
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onSelect(exercise) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = exercise.name,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black,
                                Color.Transparent
                            )
                        )
                    )
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun ECStatsContainer(
    reps: List<ECRepData>,
    setSummary: ECSetData
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 350.dp)
            .background(Color.Black.copy(alpha = 0.3f))
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F0F0F))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                ECSectionHeader(
                    title = "Set Number: Set ${setSummary.setIndex},  Weight: ${setSummary.weightKg?.toInt()} kg"
                )

                Spacer(Modifier.height(12.dp))

                ECRepsTable(reps)

                Spacer(Modifier.height(16.dp))

                ECSectionHeader(title = "Set Summary:")

                Spacer(Modifier.height(8.dp))

                ECSetSummary(setSummary)
            }
        }
    }
}

@Composable
fun ECSectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFC63758))
            .padding(vertical = 6.dp, horizontal = 8.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ECRepsTable(reps: List<ECRepData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        ECRepsHeaderRow()

        reps.forEach { rep ->
            ECRepsDataRow(rep)
        }
    }
}

@Composable
fun ECRepsHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.35f))
            .padding(vertical = 6.dp)
    ) {
        ECRepsHeaderCell("Rep", Modifier.weight(1f))
        ECRepsHeaderCell("Eccentric", Modifier.weight(1f))
        ECRepsHeaderCell("Concentric", Modifier.weight(1f))
        ECRepsHeaderCell("Tempo", Modifier.weight(1f))
        ECRepsHeaderCell("TUT", Modifier.weight(1f))
        ECRepsHeaderCell("ROM", Modifier.weight(1f))
    }
}

@Composable
fun ECRepsHeaderCell(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFFFF4D4D),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ECRepsDataRow(rep: ECRepData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        ECRepsCell(rep.repIndex.toString(), Modifier.weight(1f))
        ECRepsCell("%.1f s".format(rep.eccentricMs / 1000), Modifier.weight(1f))
        ECRepsCell("%.1f s".format(rep.concentricMs / 1000), Modifier.weight(1f))
        ECRepsCell("3-1-1-1", Modifier.weight(1f)) // placeholder tempo
        ECRepsCell("%.1f s".format(rep.tutMs / 1000), Modifier.weight(1f))
        ECRepsCell("%.0f°".format(rep.romDeg), Modifier.weight(1f))
    }
}

@Composable
fun ECRepsCell(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ECSetSummary(data: ECSetData) {
    val avgEcc = data.reps.map { it.eccentricMs }.average() / 1000
    val avgCon = data.reps.map { it.concentricMs }.average() / 1000
    val totalTUT = data.reps.sumOf { it.tutMs } / 1000
    val avgTUT = data.reps.map { it.tutMs }.average() / 1000
    val tempoConsistency = 0.22 // placeholder

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ECSetSummaryRow("Avg Eccentric:", "%.2f s".format(avgEcc))
        ECSetSummaryRow("Avg Concentric:", "%.2f s".format(avgCon))
        ECSetSummaryRow("Avg TUT/rep:", "%.2f s".format(avgTUT))
        ECSetSummaryRow("Total TUT:", "%.1f s".format(totalTUT))
        ECSetSummaryRow("Tempo Consistency:", "± %.2f s".format(tempoConsistency))

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Note: Good overall control",
            color = Color.White,
            fontSize = 13.sp
        )
    }
}

@Composable
fun ECSetSummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White, fontSize = 13.sp)
        Text(value, color = Color.White, fontSize = 13.sp)
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
        val context = LocalContext.current
        val prefs = UserPreferencesDataStore(context)
        val userId by prefs.userIdFlow.collectAsState(initial = null)

        val navController = rememberNavController()

        NavBar(
            user_name = if (userId != null) "Jake" else "Guest",
            is_dark_mode = true,
            show_back_button = true,
            nav_controller = navController,
            loggedInUserId = userId,
            on_profile_click = {
                navController.navigate("login")
            },
            on_settings_click = {
                navController.navigate("settings/$userId")
            }
        )
    }

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ECStaticVideoTestScreen() {
    val context = LocalContext.current

    var isPlaying by remember { mutableStateOf(false) }
    var detectionText by remember { mutableStateOf("Static Video Mode") }

    var reps by remember { mutableStateOf(emptyList<ECRepData>()) }
    val repDetector = remember { ECRepDetector() }

    val scope = rememberCoroutineScope()

    val videoUri = remember {
        Uri.parse("android.resource://" + context.packageName + "/raw/bench")
    }

    val analyzer = remember {
        ObjectRecognitionAnalyzer(
            context = context,
            onDetectedObjectUpdated = { label ->
                detectionText = label
            },
            onFrameMetrics = { metrics ->
                val rep = repDetector.onSample(metrics)
                if (rep != null) {
                    reps = reps + rep
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Text(
            text = "Static EC Test",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = detectionText,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = {
                VideoView(it).apply {
                    setVideoURI(videoUri)
                    setOnPreparedListener { mp ->
                        mp.isLooping = true
                    }
                }
            },
            update = { videoView ->
                if (isPlaying) {
                    videoView.start()
                }
            }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                isPlaying = true
                scope.launch {
                    runVideoAnalysis(context, analyzer, videoUri)
                }
            }
        ) {
            Text("Start Analysis")
        }

        Spacer(Modifier.height(32.dp))

        if (reps.isNotEmpty()) {
            ECStatsContainer(
                reps = reps,
                setSummary = ECSetData(
                    workoutExerciseId = 1,
                    weightKg = 60.0,
                    setIndex = 1,
                    reps = reps,
                    rir = null,
                    rpe = null
                )
            )
        } else {
            Text(
                text = "No reps detected yet.",
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ECStaticVideoInlinePlayer(
    modifier: Modifier = Modifier,
    videoResId: Int,
    onLabelUpdated: (String) -> Unit,
    onFrameMetrics: (ECFrameMetrics) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val videoUri = remember(videoResId) {
        Uri.parse("android.resource://${context.packageName}/$videoResId")
    }

    val analyzer = remember(videoResId) {
        ObjectRecognitionAnalyzer(
            context = context,
            onDetectedObjectUpdated = onLabelUpdated,
            onFrameMetrics = onFrameMetrics
        )
    }

    val videoViewRef = remember { mutableStateOf<VideoView?>(null) }

    Box(modifier = modifier.background(Color.Black)) {

        AndroidView(
            modifier = Modifier.matchParentSize(),
            factory = { ctx ->
                VideoView(ctx).apply {
                    videoViewRef.value = this
                    setVideoURI(videoUri)
                    setOnPreparedListener { mp ->
                        mp.isLooping = true
                        mp.start()
                    }
                }
            },
            update = { videoView ->
                videoView.stopPlayback()
                videoView.setVideoURI(videoUri)
                videoView.start()
            }
        )

        LaunchedEffect(videoResId) {
            scope.launch {
                runVideoAnalysis(context, analyzer, videoUri)
            }
        }
    }
}

suspend fun runVideoAnalysis(
    context: Context,
    analyzer: ObjectRecognitionAnalyzer,
    videoUri: Uri
) = withContext(Dispatchers.IO) {

    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, videoUri)

    val durationMs = retriever
        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        ?.toLongOrNull() ?: return@withContext

    var timeUs = 0L
    val stepUs = 33_000L  // ~30 FPS

    Log.d("EC-Video", "Starting static video analysis for $durationMs ms")

    while (timeUs < durationMs * 1000) {
        val frameBitmap = retriever.getFrameAtTime(
            timeUs,
            MediaMetadataRetriever.OPTION_CLOSEST
        )

        if (frameBitmap != null) {
            analyzer.detectObjects(frameBitmap)
        }

        timeUs += stepUs
        delay(5)
    }

    Log.d("EC-Video", "Finished static video analysis")
}

@Composable
fun StaticVideoDropdownMenu(
    videos: List<Pair<String, Int>>,
    onSelect: (Pair<String, Int>) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable(onClick = { onDismiss() }, indication = null, interactionSource = remember { MutableInteractionSource() })
    ) {

        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
                .height(350.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F0F0F))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                items(videos) { pair ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1A1A1A))
                            .clickable { onSelect(pair) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pair.first,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}


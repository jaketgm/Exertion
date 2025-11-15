package com.example.exertion.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.exertion.R
import com.example.exertion.data.ec.ECRepData
import com.example.exertion.data.ec.ECSetData
import com.example.exertion.data.ec.saveECSetToDB
import com.example.exertion.data.exercise.ExerciseTable
import com.example.exertion.data.exercise.ExerciseVM
import com.example.exertion.data.rep_entry.RepEntryVM
import com.example.exertion.data.set_entry.SetEntryVM
import com.example.exertion.ui_components.buttons.EccentricConcentricButton
import com.example.exertion.ui_components.navbar.NavBar
import com.example.exertion.utils.camera.InlineCameraPreview
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EccentricConcentricEvaluatorScreen(
    navController: NavController? = null,
    exerciseName: String = "Bench Press"
) {
    var isDetecting by remember { mutableStateOf(false) }
    var detectionText by remember { mutableStateOf("Exercise: $exerciseName") }

    val setVM: SetEntryVM = viewModel()
    val repVM: RepEntryVM = viewModel()
    val exerciseVM: ExerciseVM = viewModel()
    val exercises by exerciseVM.allExercises.collectAsState(initial = emptyList())
    var showExerciseMenu by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
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
            if (isDetecting) {
                InlineCameraPreview(
                    modifier = Modifier.matchParentSize(),
                    onDetectionUpdated = { detected ->
                        detectionText = detected
                    }
                )
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
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        Spacer(Modifier.height(40.dp))

        val placeholderSet = ECSetData(
            workoutExerciseId = 1,   // Temporary
            setIndex = 1,
            reps = listOf(
                ECRepData(
                    repIndex = 1,
                    eccentricMs = 1000.0,
                    concentricMs = 800.0,
                    tutMs = 1800.0,
                    velocity = 0.35,
                    romDeg = 60.0
                )
            ),
            weightKg = 60.0,
            rir = 2.0,
            rpe = 8.0
        )

        ECActionBar(
            onSaveData = {
                scope.launch {
                    saveECSetToDB(
                        setVM = setVM,
                        repVM = repVM,
                        data = placeholderSet
                    )
                }
            },
            onSelectExercise = {
                showExerciseMenu = true
            }
        )

        if (showExerciseMenu) {
            ExerciseDropdownMenu(
                exercises = exercises,
                onSelect = { selected ->
                    detectionText = "Exercise: ${selected.name}"
                    showExerciseMenu = false
                },
                onDismiss = { showExerciseMenu = false }
            )
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
            // Scrollable content
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

            // Top fade
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

            // Bottom fade
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
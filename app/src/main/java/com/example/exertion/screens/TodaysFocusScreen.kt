package com.example.exertion.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.exertion.data.datastore.UserPreferencesDataStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exertion.data.db.relations.WorkoutExerciseWithSets
import com.example.exertion.data.set_entry.SetEntryVM
import com.example.exertion.data.workout.WorkoutVM
import com.example.exertion.data.workout_exercise.WorkoutExerciseVM
import com.example.exertion.ui_components.cards.ExerciseCard
import com.example.exertion.ui_components.navbar.NavBar
import kotlinx.coroutines.launch

import com.example.exertion.data.ec.UISetRow
import com.example.exertion.data.ec.UIExerciseBlock


// --------------------------------------------------------
// HELPERS
// --------------------------------------------------------

fun estimateOneRm(weight: Double?, reps: Int): Double? {
    if (weight == null || reps <= 1) return weight
    return weight * (1 + reps / 30.0)
}

private fun convertToUI(exercises: List<WorkoutExerciseWithSets>): List<UIExerciseBlock> {
    return exercises.map { wex ->

        val uiSets = wex.sets.sortedBy { it.set_index }.map { set ->
            val est1rm = estimateOneRm(set.weight_kg, set.reps)
            val pct = if (est1rm != null && est1rm > 0 && set.weight_kg != null)
                ((set.weight_kg / est1rm) * 100).toInt()
            else null

            UISetRow(
                setId = set.set_id,
                setIndex = set.set_index,
                reps = set.reps.toString(),
                weight = set.weight_kg ?: 0.0,
                restSeconds = set.rest_sec?.toInt(),
                oneRmPercent = pct
            )
        }

        UIExerciseBlock(
            workoutExerciseId = wex.workoutExercise.workout_exercise_id,
            exerciseId = wex.workoutExercise.exercise_id,
            name = "Exercise ${wex.workoutExercise.exercise_id}", // TODO join ExerciseTable
            sets = uiSets
        )
    }
}


// --------------------------------------------------------
// MAIN SCREEN
// --------------------------------------------------------

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodaysFocusScreen(
    navController: NavController,
    workoutExerciseVM: WorkoutExerciseVM = viewModel(),
    setEntryVM: SetEntryVM = viewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val prefs = UserPreferencesDataStore(context)
    val userId by prefs.userIdFlow.collectAsState(initial = null)

    val workoutVM: WorkoutVM = viewModel()

    var todayWorkoutId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(userId) {
        if (userId != null) {
            todayWorkoutId = workoutVM.getOrCreateWorkoutForToday(userId!!)
        }
    }

    // RAW DB RESULT
    val rawExercises by workoutExerciseVM
        .observeExercisesWithSets(todayWorkoutId)
        .collectAsState(initial = emptyList())

    // UI-MAPPED DATA
    val uiExercises = remember(rawExercises) { convertToUI(rawExercises) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        NavBar(
            user_name = if (userId != null) "Jake" else "Guest",
            is_dark_mode = true,
            show_back_button = true,
            nav_controller = navController,
            loggedInUserId = userId,
            on_profile_click = { navController.navigate("login") },
            on_settings_click = { navController.navigate("settings/$userId") }
        )

        Spacer(Modifier.height(14.dp))

        // ---------------------------
        // EXERCISE LIST
        // ---------------------------
        uiExercises.forEachIndexed { index, ex ->

            ExerciseCard(
                exercise = ex,
                onMoveUp = {
                    if (index > 0) {
                        // TODO implement reorder logic
                    }
                },
                onMoveDown = {
                    if (index < uiExercises.lastIndex) {
                        // TODO implement reorder logic
                    }
                },
                onAddSet = {
                    scope.launch {
                        setEntryVM.addSetForWorkoutExercise(ex.workoutExerciseId)
                    }
                },
                onEvaluateEC = { setIndex ->
                    navController.navigate("eccentric_concentric")
                }
            )

            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(20.dp))

        AddExerciseButton(
            onAdd = {
                scope.launch {
                    if (todayWorkoutId != null) {
                        workoutExerciseVM.addWorkoutExerciseSimple(
                            workoutId = todayWorkoutId!!,
                            exerciseId = 1 // Bench Press
                        )
                    }
                }
            }
        )

        Spacer(Modifier.height(60.dp))
    }
}


@Composable
fun AddExerciseButton(onAdd: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF292929))
            .clickable { onAdd() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+ Add Exercise",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

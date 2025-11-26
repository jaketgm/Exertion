package com.example.exertion.data.ec

import androidx.room.Embedded
import androidx.room.Relation
import com.example.exertion.data.exercise.ExerciseTable
import com.example.exertion.data.set_entry.SetEntry
import com.example.exertion.data.workout_exercise.WorkoutExercise

data class ECRepData(
    val repIndex: Int,
    val eccentricMs: Double,
    val concentricMs: Double,
    val tutMs: Double,
    val velocity: Double?,
    val romDeg: Double?
)

data class ECSetData(
    val workoutExerciseId: Int,
    val weightKg: Double?,
    val setIndex: Int,
    val reps: List<ECRepData>,
    val rir: Double? = null,
    val rpe: Double? = null
)

enum class ECPhase {
    IDLE,
    ECCENTRIC,
    CONCENTRIC
}

data class ECFrameMetrics(
    val timestampMs: Long,
    val romFraction: Float, // (0 = bottom, 1 = top), I think this works
    val velocity: Float, // change in romFraction / second
    val phase: ECPhase
)

data class UIExerciseBlock(
    val workoutExerciseId: Int,
    val exerciseId: Int,
    val name: String,
    val sets: List<UISetRow>
)

data class UISetRow(
    val setId: Int,
    val setIndex: Int,
    val reps: String,
    val weight: Double,
    val restSeconds: Int?,
    val oneRmPercent: Int?
)

data class WorkoutExerciseFull(
    @Embedded val workoutExercise: WorkoutExercise,
    @Relation(
        parentColumn = "exercise_id",
        entityColumn = "exercise_id"
    )
    val exercise: ExerciseTable,
    @Relation(
        parentColumn = "workout_exercise_id",
        entityColumn = "workout_exercise_id"
    )
    val sets: List<SetEntry>
)

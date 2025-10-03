package com.example.exertion.data.workout_exercise

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*
import com.example.exertion.data.exercise.ExerciseTable
import com.example.exertion.data.workout.Workout

@Entity(
    tableName = "workout_exercise",
    foreignKeys = [
        // WORKOUT.workout_id < WORKOUT_EXERCISE.workout_id [delete: cascade]
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workout_id"],
            childColumns = ["workout_id"],
            onDelete = ForeignKey.CASCADE
        ),
        // EXERCISE.exercise_id < WORKOUT_EXERCISE.exercise_id [delete: restrict]
        ForeignKey(
            entity = ExerciseTable::class,
            parentColumns = ["exercise_id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        // Enforce one row per position in a workout (allows same exercise multiple times if orders differ)
        Index(value = ["workout_id", "exercise_order"], unique = true),

        // Extra helpers per DBML
        Index(value = ["workout_id"]),
        Index(value = ["exercise_id"])
    ]
)
data class WorkoutExercise(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_exercise_id")
    val workout_exercise_id: Int = 0,

    @ColumnInfo(name = "workout_id")
    val workout_id: Int, // Foreign Key -> WORKOUT

    @ColumnInfo(name = "exercise_id")
    val exercise_id: Int, // Foreign Key -> EXERCISE

    @ColumnInfo(name = "exercise_order")
    val exercise_order: Int,

    @ColumnInfo(name = "target_sets")
    val target_sets: Int?,

    @ColumnInfo(name = "target_reps")
    val target_reps: Int?,

    @ColumnInfo(name = "target_weight")
    val target_weight: Double?,
)
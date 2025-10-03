package com.example.exertion.data.set_entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*
import com.example.exertion.data.workout_exercise.WORKOUT_EXERCISE

@Entity(
    tableName = "set_entry",
    foreignKeys = [
        ForeignKey(
            entity = WORKOUT_EXERCISE::class,
            parentColumns = ["workout_exercise_id"],
            childColumns = ["workout_exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["workout_exercise_id", "set_index"], unique = true),
        Index(value = ["workout_exercise_id"])
    ]
)
data class SetEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "set_id")
    val set_id: Int = 0,

    @ColumnInfo(name = "workout_exercise_id")
    val workout_exercise_id: Int, // Foreign key -> WORKOUT_EXERCISE

    @ColumnInfo(name = "set_index")
    val set_index: Int,

    @ColumnInfo(name = "set_type")
    val set_type: String?,

    @ColumnInfo(name = "reps")
    val reps: Int, // >= 0

    @ColumnInfo(name = "weight_kg")
    val weight_kg: Double?,

    @ColumnInfo(name = "tut_ms")
    val tut_ms: Double?,

    @ColumnInfo(name = "rir")
    val rir: Double?, // Allowed range ~ [-5..10]

    @ColumnInfo(name = "rpe")
    val rpe: Double?, // Allowed range ~ [1..10]

    @ColumnInfo(name = "rest_sec")
    val rest_sec: Float?,

    @ColumnInfo(name = "is_warmup", defaultValue = "0")
    val is_warmup: Boolean = false,

    @ColumnInfo(name = "is_failure", defaultValue = "0")
    val is_failure: Boolean = false,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "top", defaultValue = "0.0")
    val top: Double = 0.0,

    @ColumnInfo(name = "bottom", defaultValue = "0.0")
    val bottom: Double = 0.0,

    @ColumnInfo(name = "tempo_notation")
    val tempo_notation: String?,

    @ColumnInfo(name = "velocity_loss_pct")
    val velocity_loss_pct: Double?, // 0..100, from rep velocities

    @ColumnInfo(name = "rir_suggested")
    val rir_suggested: Double?,

    @ColumnInfo(name = "rpe_suggested")
    val rpe_suggested: Double?,

    @ColumnInfo(name = "suggestion_confidence")
    val suggestion_confidence: Double? // 0..1 aggregate confidence
)
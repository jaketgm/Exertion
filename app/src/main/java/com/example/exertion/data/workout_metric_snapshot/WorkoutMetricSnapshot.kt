package com.example.exertion.data.workout_metric_snapshot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*
import com.example.exertion.data.user_table.UserTable
import com.example.exertion.data.workout.Workout

@Entity(
    tableName = "WORKOUT_METRIC_SNAPSHOT",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workout_id"],
            childColumns = ["workout_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserTable::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["workout_id"], unique = true),
        Index(value = ["user_id", "completed_at"]),
        Index(value = ["is_stale"])
    ]
)
data class WorkoutMetricSnapshot(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "snapshot_id")
    val snapshot_id: Int = 0,

    @ColumnInfo(name = "workout_id")
    val workout_id: Int, // FK -> WORKOUT (one per workout)

    @ColumnInfo(name = "user_id")
    val user_id: Int, // denormalized for fast filters

    @ColumnInfo(name = "completed_at")
    val completed_at: Long,

    @ColumnInfo(name = "total_volume_kg", defaultValue = "0.0")
    val total_volume_kg: Double = 0.0,

    @ColumnInfo(name = "total_sets", defaultValue = "0.0")
    val total_sets: Double = 0.0,

    @ColumnInfo(name = "avg_rest_time")
    val avg_rest_time: Double?,

    @ColumnInfo(name = "percent_failure")
    val percent_failure: Double?, // 0..1 of sets with is_failure = true

    @ColumnInfo(name = "total_tut_ms")
    val total_tut_ms: Double?,

    @ColumnInfo(name = "avg_concentric_ms")
    val avg_concentric_ms: Double?,

    @ColumnInfo(name = "avg_eccentric_ms")
    val avg_eccentric_ms: Double?,

    @ColumnInfo(name = "velocity_peak_mps")
    val velocity_peak_mps: Double?,

    @ColumnInfo(name = "velocity_avg_mps")
    val velocity_avg_mps: Double?,

    @ColumnInfo(name = "est_1rm_top_kg")
    val est_1rm_top_kg: Double?,

    @ColumnInfo(name = "dots")
    val dots: Double?,

    @ColumnInfo(name = "avg_rir")
    val avg_rir: Double?,

    @ColumnInfo(name = "avg_rpe")
    val avg_rpe: Double?,

    @ColumnInfo(name = "kcal_burned")
    val kcal_burned: Double?,

    @ColumnInfo(name = "is_stale", defaultValue = "0")
    val is_stale: Boolean = false,

    @ColumnInfo(name = "computed_at")
    val computed_at: Long,
)
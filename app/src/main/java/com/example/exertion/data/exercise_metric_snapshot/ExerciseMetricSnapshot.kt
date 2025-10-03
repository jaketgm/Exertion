package com.example.exertion.data.exercise_metric_snapshot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.exertion.data.exercise.ExerciseTable
import com.example.exertion.data.user_table.UserTable

@Entity(
    tableName = "exercise_metric_snapshot",
    foreignKeys = [
        ForeignKey(
            entity = UserTable::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseTable::class,
            parentColumns = ["exercise_id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(
            value = ["user_id", "exercise_id", "period_start", "period_end"],
            unique = true
        ),
        Index(value = ["user_id", "exercise_id", "period_start"])
    ]
)
data class ExerciseMetricSnapshot(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "snapshot_id")
    val snapshot_id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Int,

    @ColumnInfo(name = "exercise_id")
    val exercise_id: Int,

    @ColumnInfo(name = "period_start")
    val period_start: Long, // epoch millis (e.g., week start)

    @ColumnInfo(name = "period_end")
    val period_end: Long,

    @ColumnInfo(name = "total_volume_kg", defaultValue = "0.0")
    val total_volume_kg: Double = 0.0,

    @ColumnInfo(name = "top_est_1rm_kg")
    val top_est_1rm_kg: Double?,

    @ColumnInfo(name = "avg_tut_ms")
    val avg_tut_ms: Double?,

    @ColumnInfo(name = "sessions", defaultValue = "0")
    val sessions: Int = 0,

    @ColumnInfo(name = "is_stale", defaultValue = "0")
    val is_stale: Boolean = false,

    @ColumnInfo(name = "computed_at")
    val computed_at: Long // epoch millis, last computation time
)

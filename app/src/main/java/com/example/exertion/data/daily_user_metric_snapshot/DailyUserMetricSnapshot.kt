package com.example.exertion.data.daily_user_metric_snapshot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.exertion.data.user_table.UserTable

@Entity(
    tableName = "daily_user_metric_snapshot",
    foreignKeys = [
        ForeignKey(
            entity = UserTable::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id", "date_utc"], unique = true) // unique per user/day
    ]
)
data class DailyUserMetricSnapshot(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "snapshot_id")
    val snapshot_id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Int,

    // yyyyMMdd as Int, or epoch-day
    @ColumnInfo(name = "date_utc")
    val date_utc: Int,

    @ColumnInfo(name = "total_volume_kg", defaultValue = "0.0")
    val total_volume_kg: Double = 0.0,

    @ColumnInfo(name = "total_sets", defaultValue = "0")
    val total_sets: Int = 0,

    @ColumnInfo(name = "total_reps", defaultValue = "0")
    val total_reps: Int = 0,

    @ColumnInfo(name = "total_tut_ms")
    val total_tut_ms: Double?,

    @ColumnInfo(name = "avg_velocity_mps")
    val avg_velocity_mps: Double?,

    @ColumnInfo(name = "est_1rm_best_kg")
    val est_1rm_best_kg: Double?,

    @ColumnInfo(name = "dots")
    val dots: Double?,

    @ColumnInfo(name = "is_stale", defaultValue = "0")
    val is_stale: Boolean = false,

    // last computation time (epoch millis)
    @ColumnInfo(name = "computed_at")
    val computed_at: Long
)

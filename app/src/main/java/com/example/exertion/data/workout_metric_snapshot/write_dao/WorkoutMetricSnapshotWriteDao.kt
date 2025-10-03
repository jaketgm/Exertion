package com.example.exertion.data.workout_metric_snapshot.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.exertion.data.workout_metric_snapshot.WorkoutMetricSnapshot

@Dao
interface WorkoutMetricSnapshotWriteDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addWorkoutMetricSnapshot(workoutMetricSnapshot: WorkoutMetricSnapshot)
}
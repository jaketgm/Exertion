package com.example.exertion.data.exercise_metric_snapshot.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.exertion.data.exercise_metric_snapshot.ExerciseMetricSnapshot

@Dao
interface ExerciseMetricSnapshotWriteDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addExerciseMetricSnapshot(exerciseMetricSnapshot: ExerciseMetricSnapshot)
}
package com.example.exertion.data.exercise_metric_snapshot.read_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.exercise_metric_snapshot.ExerciseMetricSnapshot
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseMetricSnapshotReadDao {
    @Query(
        "SELECT * FROM EXERCISE_METRIC_SNAPSHOT ORDER BY snapshot_id ASC"
    )
    fun readAllExerciseMetricSnapshotData(): Flow<List<ExerciseMetricSnapshot>>
}
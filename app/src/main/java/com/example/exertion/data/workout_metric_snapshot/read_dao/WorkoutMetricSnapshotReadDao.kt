package com.example.exertion.data.workout_metric_snapshot.read_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.workout_metric_snapshot.WorkoutMetricSnapshot

@Dao
interface WorkoutMetricSnapshotReadDao {
    @Query(
        "SELECT * FROM WORKOUT_METRIC_SNAPSHOT ORDER BY snapshot_id ASC"
    )
    fun readAllWorkoutMetricSnapshotData(): LiveData<List<WorkoutMetricSnapshot>>
}
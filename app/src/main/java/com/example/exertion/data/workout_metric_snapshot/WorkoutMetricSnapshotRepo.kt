package com.example.exertion.data.workout_metric_snapshot

import androidx.lifecycle.LiveData
import com.example.exertion.data.workout_metric_snapshot.read_dao.WorkoutMetricSnapshotReadDao
import com.example.exertion.data.workout_metric_snapshot.write_dao.WorkoutMetricSnapshotWriteDao

class WorkoutMetricSnapshotRepo(
    private val workoutMetricSnapshotReadDao: WorkoutMetricSnapshotReadDao,
    private val workoutMetricSnapshotWriteDao: WorkoutMetricSnapshotWriteDao
) {
    fun readAllWorkoutMetricSnapshotData(): LiveData<List<WorkoutMetricSnapshot>> = workoutMetricSnapshotReadDao.readAllWorkoutMetricSnapshotData()

    suspend fun addWorkoutMetricSnapshot(workoutMetricSnapshot: WorkoutMetricSnapshot) {
        workoutMetricSnapshotWriteDao.addWorkoutMetricSnapshot(workoutMetricSnapshot)
    }
}
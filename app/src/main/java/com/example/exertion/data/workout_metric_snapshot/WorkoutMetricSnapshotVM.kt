package com.example.exertion.data.workout_metric_snapshot

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.exertion.data.ExertionDB

class WorkoutMetricSnapshotVM(application: Application): AndroidViewModel(application) {
    private val read_all_workoutMetricSnapshot_data: LiveData<List<WorkoutMetricSnapshot>>
    private val workout_metric_snapshot_repository: WorkoutMetricSnapshotRepo

    init {
        val db = ExertionDB.getDatabase(application)
        val workoutMetricSnapshotReadDao = db.workoutMetricSnapshotReadDao()
        val workoutMetricSnapshotWriteDao = db.workoutMetricSnapshotWriteDao()
        workout_metric_snapshot_repository = WorkoutMetricSnapshotRepo(workoutMetricSnapshotReadDao, workoutMetricSnapshotWriteDao)
        read_all_workoutMetricSnapshot_data = workout_metric_snapshot_repository.readAllWorkoutMetricSnapshotData()
    }

    suspend fun addWorkoutMetricSnapshot(workoutMetricSnapshot: WorkoutMetricSnapshot) {
        workout_metric_snapshot_repository.addWorkoutMetricSnapshot(workoutMetricSnapshot)
    }
}
package com.example.exertion.data.exercise_metric_snapshot

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.exertion.data.EXERTION_DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExerciseMetricSnapshotVM(application: Application): AndroidViewModel(application) {
    private val read_all_exerciseMetricSnapshot_data: Flow<List<ExerciseMetricSnapshot>>
    private val exercise_metric_snapshot_repository: ExerciseMetricSnapshotRepo

    init {
        val db = EXERTION_DB.getDatabase(application)
        val exerciseMetricSnapshotReadDao = db.exerciseMetricSnapshotReadDao()
        val exerciseMetricSnapshotWriteDao = db.exerciseMetricSnapshotWriteDao()
        exercise_metric_snapshot_repository = ExerciseMetricSnapshotRepo(exerciseMetricSnapshotReadDao, exerciseMetricSnapshotWriteDao)
        read_all_exerciseMetricSnapshot_data = exercise_metric_snapshot_repository.readAllExerciseMetricSnapshotData()
    }

    suspend fun addExerciseMetricSnapshot(exerciseMetricSnapshot: ExerciseMetricSnapshot) {
        exercise_metric_snapshot_repository.addExerciseMetricSnapshot(exerciseMetricSnapshot)
    }
}
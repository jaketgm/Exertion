package com.example.exertion.data.exercise_metric_snapshot

import com.example.exertion.data.exercise_metric_snapshot.read_dao.ExerciseMetricSnapshotReadDao
import com.example.exertion.data.exercise_metric_snapshot.write_dao.ExerciseMetricSnapshotWriteDao
import kotlinx.coroutines.flow.Flow

class ExerciseMetricSnapshotRepo(
    private val exerciseMetricSnapshotReadDao: ExerciseMetricSnapshotReadDao,
    private val exerciseMetricSnapshotWriteDao: ExerciseMetricSnapshotWriteDao
) {
    fun readAllExerciseMetricSnapshotData(): Flow<List<ExerciseMetricSnapshot>> = exerciseMetricSnapshotReadDao.readAllExerciseMetricSnapshotData()

    suspend fun addExerciseMetricSnapshot(exerciseMetricSnapshot: ExerciseMetricSnapshot) {
        exerciseMetricSnapshotWriteDao.addExerciseMetricSnapshot(exerciseMetricSnapshot)
    }
}
package com.example.exertion.data.exercise

import androidx.lifecycle.LiveData
import com.example.exertion.data.exercise.read_dao.ExerciseReadDao
import com.example.exertion.data.exercise.write_dao.ExerciseWriteDao
import kotlinx.coroutines.flow.Flow

class ExerciseRepo(
    private val exerciseReadDao: ExerciseReadDao,
    private val exerciseWriteDao: ExerciseWriteDao
) {
    fun readAllExerciseData(): Flow<List<ExerciseTable>> = exerciseReadDao.readAllExerciseData()

    suspend fun addExercise(exerciseTable: ExerciseTable) {
        exerciseReadDao.addExercise(exerciseTable)
    }
}
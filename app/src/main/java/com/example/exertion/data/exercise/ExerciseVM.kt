package com.example.exertion.data.exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exertion.data.ExertionDB
import kotlinx.coroutines.flow.Flow

class ExerciseVM(application: Application): AndroidViewModel(application) {
    private val read_all_exerciseTable_data: Flow<List<ExerciseTable>>
    private val exercise_repository: ExerciseRepo

    init {
        val db = ExertionDB.getDatabase(application)
        val exerciseReadDao = db.exerciseReadDao()
        val exerciseWriteDao = db.exerciseWriteDao()
        exercise_repository = ExerciseRepo(exerciseReadDao, exerciseWriteDao)
        read_all_exerciseTable_data = exercise_repository.readAllExerciseData()
    }

    suspend fun addExercise(exercise: ExerciseTable) {
        exercise_repository.addExercise(exercise)
    }
}
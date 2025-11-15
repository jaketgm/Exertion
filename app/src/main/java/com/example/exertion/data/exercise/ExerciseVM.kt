package com.example.exertion.data.exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exertion.data.ExertionDB
import kotlinx.coroutines.flow.Flow

class ExerciseVM(application: Application): AndroidViewModel(application) {
    private val exercise_repository: ExerciseRepo

    val allExercises: Flow<List<ExerciseTable>>

    init {
        val db = ExertionDB.getDatabase(application)
        val exerciseReadDao = db.exerciseReadDao()
        val exerciseWriteDao = db.exerciseWriteDao()
        exercise_repository = ExerciseRepo(exerciseReadDao, exerciseWriteDao)
        allExercises = exercise_repository.readAllExerciseData()
    }

    suspend fun addExercise(exercise: ExerciseTable) {
        exercise_repository.addExercise(exercise)
    }
}
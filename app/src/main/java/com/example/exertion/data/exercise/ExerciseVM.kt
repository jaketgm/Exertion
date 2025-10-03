package com.example.exertion.data.exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.exertion.data.EXERTION_DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExerciseVM(application: Application): AndroidViewModel(application) {
    private val read_all_exerciseTable_data: Flow<List<ExerciseTable>>
    private val exercise_repository: ExerciseRepo

    init {
        val db = EXERTION_DB.getDatabase(application)
        val exerciseReadDao = db.exerciseReadDao()
        val exerciseWriteDao = db.exerciseWriteDao()
        exercise_repository = ExerciseRepo(exerciseReadDao, exerciseWriteDao)
        read_all_exerciseTable_data = exercise_repository.readAllExerciseData()
    }

    suspend fun addExercise(exercise: ExerciseTable) {
        exercise_repository.addExercise(exercise)
    }
}
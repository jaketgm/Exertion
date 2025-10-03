package com.example.exertion.data.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.exertion.data.EXERTION_DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WORKOUT_VM(application: Application): AndroidViewModel(application) {
    private val read_all_workout_data: LiveData<List<WORKOUT>>
    private val workout_repository: WORKOUT_REPO

    init {
        val workoutDao = EXERTION_DB.getDatabase(application).workoutDao()
        workout_repository = WORKOUT_REPO(workoutDao)
        read_all_workout_data = workout_repository.read_all_workout_data
    }

    fun addWorkout(workout: WORKOUT) {
        viewModelScope.launch(Dispatchers.IO) {
            workout_repository.addWorkout(workout)
        }
    }
}
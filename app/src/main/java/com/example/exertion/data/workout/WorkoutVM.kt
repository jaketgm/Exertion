package com.example.exertion.data.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exertion.data.ExertionDB
import kotlinx.coroutines.flow.Flow

class WorkoutVM(application: Application): AndroidViewModel(application) {
    private val read_all_workout_data: Flow<List<Workout>>
    private val workout_repository: WorkoutRepo

    init {
        val db = ExertionDB.getDatabase(application)
        val workoutReadDao = db.workoutReadDao()
        val workoutWriteDao = db.workoutWriteDao()
        workout_repository = WorkoutRepo(workoutReadDao, workoutWriteDao)
        read_all_workout_data = workout_repository.readAllWorkoutData()
    }

    suspend fun addWorkout(workout: Workout) {
        workout_repository.addWorkout(workout)
    }
}
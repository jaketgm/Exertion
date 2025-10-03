package com.example.exertion.data.workout

import androidx.lifecycle.LiveData

class WORKOUT_REPO(private val workoutDao: WORKOUT_DAO) {
    val read_all_workout_data: LiveData<List<WORKOUT>> = workoutDao.readAllWorkoutData()

    suspend fun addWorkout(workout: WORKOUT) {
        workoutDao.addWorkout(workout)
    }
}
package com.example.exertion.data.workout

import androidx.lifecycle.LiveData
import com.example.exertion.data.workout.read_dao.WorkoutReadDao
import com.example.exertion.data.workout.write_dao.WorkoutWriteDao
import kotlinx.coroutines.flow.Flow

class WorkoutRepo(
    private val workoutReadDao: WorkoutReadDao,
    private val workoutWriteDao: WorkoutWriteDao
) {
    fun readAllWorkoutData(): Flow<List<Workout>> = workoutReadDao.readAllWorkoutData()

    suspend fun addWorkout(workout: Workout) {
        workoutWriteDao.addWorkout(workout)
    }
}
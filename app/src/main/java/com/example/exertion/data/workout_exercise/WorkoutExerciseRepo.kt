package com.example.exertion.data.workout_exercise

import androidx.lifecycle.LiveData
import com.example.exertion.data.workout_exercise.read_dao.WorkoutExerciseReadDao
import com.example.exertion.data.workout_exercise.write_dao.WorkoutExerciseWriteDao
import kotlinx.coroutines.flow.Flow

class WorkoutExerciseRepo(
    private val workoutExerciseReadDao: WorkoutExerciseReadDao,
    private val workoutExerciseWriteDao: WorkoutExerciseWriteDao
) {
    fun readAllWorkoutExerciseData(): Flow<List<WorkoutExercise>> = workoutExerciseReadDao.readAllWorkoutExerciseData()

    suspend fun addWorkoutExercise(workoutExercise: WorkoutExercise) {
        workoutExerciseWriteDao.addWorkoutExercise(workoutExercise)
    }
}
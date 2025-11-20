package com.example.exertion.data.workout_exercise

import androidx.lifecycle.LiveData
import com.example.exertion.data.db.relations.WorkoutExerciseWithSets
import com.example.exertion.data.workout_exercise.read_dao.WorkoutExerciseReadDao
import com.example.exertion.data.workout_exercise.write_dao.WorkoutExerciseWriteDao
import kotlinx.coroutines.flow.Flow

class WorkoutExerciseRepo(
    private val readDao: WorkoutExerciseReadDao,
    private val writeDao: WorkoutExerciseWriteDao
) {
    fun readAllWorkoutExerciseData(): Flow<List<WorkoutExercise>> =
        readDao.readAllWorkoutExerciseData()

    suspend fun addWorkoutExercise(workoutExercise: WorkoutExercise) =
        writeDao.addWorkoutExercise(workoutExercise)

    fun observeExercisesForWorkout(workoutId: Int): Flow<List<WorkoutExerciseWithSets>> =
        readDao.observeExercisesWithSets(workoutId)

    suspend fun updateExerciseOrder(id: Int, newOrder: Int) =
        writeDao.updateExerciseOrder(id, newOrder)
}
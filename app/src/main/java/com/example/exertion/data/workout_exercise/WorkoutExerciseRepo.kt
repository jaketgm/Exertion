package com.example.exertion.data.workout_exercise

import androidx.lifecycle.LiveData
import com.example.exertion.data.db.relations.WorkoutExerciseWithSets
import com.example.exertion.data.workout_exercise.read_dao.WorkoutExerciseReadDao
import com.example.exertion.data.workout_exercise.write_dao.WorkoutExerciseWriteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

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

    suspend fun addWorkoutExerciseSimple(workoutId: Int, exerciseId: Int) {
        val current = readDao.observeExercisesWithSets(workoutId).first()

        val maxOrder: Int? =
            current.maxOfOrNull { it.workoutExercise.exercise_order }

        val nextOrder = (maxOrder ?: 0) + 1

        val newItem = WorkoutExercise(
            workout_exercise_id = 0,
            workout_id = workoutId,
            exercise_id = exerciseId,
            exercise_order = nextOrder,
            target_sets = null,
            target_reps = null,
            target_weight = null
        )

        writeDao.addWorkoutExercise(newItem)
    }
}
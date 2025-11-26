package com.example.exertion.data.workout_exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exertion.data.ExertionDB
import com.example.exertion.data.db.relations.WorkoutExerciseWithSets
import com.example.exertion.data.ec.WorkoutExerciseFull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WorkoutExerciseVM(application: Application) : AndroidViewModel(application) {
    private val repo: WorkoutExerciseRepo

    val allWorkoutExercises: Flow<List<WorkoutExercise>>

    init {
        val db = ExertionDB.getDatabase(application)
        val readDao = db.workoutExerciseReadDao()
        val writeDao = db.workoutExerciseWriteDao()

        repo = WorkoutExerciseRepo(readDao, writeDao)
        allWorkoutExercises = repo.readAllWorkoutExerciseData()
    }

    fun observeExercisesWithSets(workoutId: Int?): Flow<List<WorkoutExerciseWithSets>> {
        return if (workoutId == null) {
            flowOf(emptyList())
        } else {
            repo.observeExercisesForWorkout(workoutId)
        }
    }

    suspend fun addWorkoutExercise(workoutExercise: WorkoutExercise) =
        repo.addWorkoutExercise(workoutExercise)

    suspend fun updateExerciseOrder(id: Int, newOrder: Int) =
        repo.updateExerciseOrder(id, newOrder)

    suspend fun addExerciseToWorkout(workoutId: Int, exerciseId: Int) {
        repo.addWorkoutExerciseSimple(workoutId, exerciseId)
    }

    fun observeExercisesFull(workoutId: Int?): Flow<List<WorkoutExerciseFull>> {
        return repo.observeExercisesFull(workoutId)
    }

    suspend fun addWorkoutExerciseSimple(workoutId: Int, exerciseId: Int) {
        repo.addWorkoutExerciseSimple(workoutId, exerciseId)
    }
}

package com.example.exertion.data.workout_exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exertion.data.ExertionDB
import kotlinx.coroutines.flow.Flow

class WorkoutExerciseVM(application: Application): AndroidViewModel(application) {
    private val read_all_workout_exercise_data: Flow<List<WorkoutExercise>>
    private val workout_exercise_repository: WorkoutExerciseRepo

    init {
        val db = ExertionDB.getDatabase(application)
        val workoutExerciseReadDao = db.workoutExerciseReadDao()
        val workoutExerciseWriteDao = db.workoutExerciseWriteDao()
        workout_exercise_repository = WorkoutExerciseRepo(workoutExerciseReadDao, workoutExerciseWriteDao)
        read_all_workout_exercise_data = workout_exercise_repository.readAllWorkoutExerciseData()
    }

    suspend fun addWorkoutExercise(workoutExercise: WorkoutExercise) {
        workout_exercise_repository.addWorkoutExercise(workoutExercise)
    }
}
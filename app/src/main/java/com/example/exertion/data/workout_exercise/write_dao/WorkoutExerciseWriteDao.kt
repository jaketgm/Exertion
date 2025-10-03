package com.example.exertion.data.workout_exercise.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.exertion.data.workout_exercise.WorkoutExercise

@Dao
interface WorkoutExerciseWriteDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addWorkoutExercise(workoutExercise: WorkoutExercise)
}
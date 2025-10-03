package com.example.exertion.data.workout_exercise.read_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.workout_exercise.WorkoutExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutExerciseReadDao {
    @Query(
        "SELECT * FROM WORKOUT_EXERCISE ORDER BY workout_exercise_id ASC"
    )
    fun readAllWorkoutExerciseData(): Flow<List<WorkoutExercise>>
}
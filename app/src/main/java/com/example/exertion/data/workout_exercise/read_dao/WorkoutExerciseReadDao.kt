package com.example.exertion.data.workout_exercise.read_dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.exertion.data.db.relations.WorkoutExerciseWithSets
import com.example.exertion.data.workout_exercise.WorkoutExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutExerciseReadDao {

    @Query(
        "SELECT * FROM workout_exercise ORDER BY workout_exercise_id ASC"
    )
    fun readAllWorkoutExerciseData(): Flow<List<WorkoutExercise>>

    @Transaction
    @Query("""
        SELECT * FROM workout_exercise
        WHERE workout_id = :workoutId
        ORDER BY exercise_order ASC
    """)
    fun observeExercisesWithSets(
        workoutId: Int
    ): Flow<List<WorkoutExerciseWithSets>>
}
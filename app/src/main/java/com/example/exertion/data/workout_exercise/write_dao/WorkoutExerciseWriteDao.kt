package com.example.exertion.data.workout_exercise.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.workout_exercise.WorkoutExercise

@Dao
interface WorkoutExerciseWriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWorkoutExercise(items: List<WorkoutExercise>): List<Long>

    @Query("""
        UPDATE workout_exercise
        SET exercise_order = :newOrder
        WHERE workout_exercise_id = :id
    """)
    suspend fun updateExerciseOrder(
        id: Int,
        newOrder: Int
    )
}
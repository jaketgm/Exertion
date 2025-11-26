package com.example.exertion.data.workout.read_dao

import androidx.room.Dao
import androidx.room.Query
import com.example.exertion.data.workout.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutReadDao {
    @Query(
        "SELECT * FROM WORKOUT ORDER BY workout_id ASC"
    )
    fun readAllWorkoutData(): Flow<List<Workout>>

    @Query("SELECT * FROM workout WHERE user_id = :userId AND day_of_week = :dayOfWeek LIMIT 1")
    suspend fun getWorkoutByDay(userId: Int, dayOfWeek: Int): Workout?

    @Query("SELECT * FROM workout WHERE user_id = :userId ORDER BY workout_id ASC LIMIT 1")
    suspend fun getFirstWorkoutForUser(userId: Int): Workout?
}
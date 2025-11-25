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

    @Query("""
        SELECT * FROM workout
        WHERE user_id = :userId
        AND date(started_at / 1000, 'unixepoch') = date(:todayEpoch / 1000, 'unixepoch')
        LIMIT 1
    """)
    suspend fun getWorkoutForToday(
        userId: Int,
        todayEpoch: Long
    ): Workout?
}
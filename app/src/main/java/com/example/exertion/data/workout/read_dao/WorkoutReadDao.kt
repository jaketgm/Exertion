package com.example.exertion.data.workout.read_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.workout.WORKOUT
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutReadDao {
    @Query(
        "SELECT * FROM WORKOUT ORDER BY workout_id ASC"
    )
    fun readAllWorkoutData(): Flow<List<WORKOUT>>
}
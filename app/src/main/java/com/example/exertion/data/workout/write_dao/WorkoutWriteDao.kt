package com.example.exertion.data.workout.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.workout.Workout

@Dao
interface WorkoutWriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWorkout(workout: Workout): Long
}
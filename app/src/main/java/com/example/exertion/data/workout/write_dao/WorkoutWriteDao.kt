package com.example.exertion.data.workout.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.exertion.data.workout.WORKOUT

@Dao
interface WorkoutWriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWorkout(workout: WORKOUT)
}
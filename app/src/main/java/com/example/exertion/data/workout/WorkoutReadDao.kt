package com.example.exertion.data.workout

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WORKOUT_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWorkout(workout: WORKOUT)

    @Query(
        "SELECT * FROM WORKOUT ORDER BY workout_id ASC"
    )
    fun readAllWorkoutData(): LiveData<List<WORKOUT>>
}
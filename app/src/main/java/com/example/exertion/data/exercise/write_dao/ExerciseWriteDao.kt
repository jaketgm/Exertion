package com.example.exertion.data.exercise.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.exertion.data.exercise.ExerciseTable

@Dao
interface ExerciseWriteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertExercises(exercises: List<ExerciseTable>): List<Long>
}
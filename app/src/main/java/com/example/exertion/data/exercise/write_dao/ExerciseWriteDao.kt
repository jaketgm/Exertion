package com.example.exertion.data.exercise.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Upsert
import com.example.exertion.data.exercise.ExerciseTable

@Dao
interface ExerciseWriteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertExercise(exercise: ExerciseTable): Long
}
package com.example.exertion.data.exercise.read_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.exercise.ExerciseTable
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseReadDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addExercise(exerciseTable: ExerciseTable)

    @Query(
        "SELECT * FROM EXERCISE ORDER BY exercise_id ASC"
    )
    fun readAllExerciseData(): Flow<List<ExerciseTable>>
}
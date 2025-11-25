package com.example.exertion.data.set_entry.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.set_entry.SetEntry

@Dao
interface SetEntryWriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSetEntries(setEntries: List<SetEntry>): List<Long>

    @Query("DELETE FROM set_entry WHERE set_id = :id")
    suspend fun deleteSetById(id: Int)

    @Query("""
        SELECT COALESCE(MAX(set_index), 0) 
        FROM set_entry 
        WHERE workout_exercise_id = :workoutExerciseId
    """)
    suspend fun getNextSetIndex(workoutExerciseId: Int): Int
}
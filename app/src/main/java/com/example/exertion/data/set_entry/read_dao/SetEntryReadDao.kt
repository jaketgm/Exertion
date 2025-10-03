package com.example.exertion.data.set_entry.read_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.set_entry.SetEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface SetEntryReadDao {
    @Query(
        "SELECT * FROM SET_ENTRY ORDER BY set_id ASC"
    )
    fun readAllSetEntryData(): Flow<List<SetEntry>>
}
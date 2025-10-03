package com.example.exertion.data.rep_entry.read_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.rep_entry.RepEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface RepEntryReadDao {
    @Query(
        "SELECT * FROM REP_ENTRY ORDER BY rep_id ASC"
    )
    fun readAllRepEntryData(): Flow<List<RepEntry>>
}
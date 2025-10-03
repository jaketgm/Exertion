package com.example.exertion.data.rep_entry.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.exertion.data.rep_entry.RepEntry

@Dao
interface RepEntryWriteDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addRepEntry(repEntry: RepEntry)
}
package com.example.exertion.data.set_entry.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.exertion.data.set_entry.SetEntry

@Dao
interface SetEntryWriteDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addSetEntry(setEntry: SetEntry)
}
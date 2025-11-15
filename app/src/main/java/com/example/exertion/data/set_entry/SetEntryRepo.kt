package com.example.exertion.data.set_entry

import androidx.lifecycle.LiveData
import com.example.exertion.data.set_entry.read_dao.SetEntryReadDao
import com.example.exertion.data.set_entry.write_dao.SetEntryWriteDao
import kotlinx.coroutines.flow.Flow

class SetEntryRepo(
    private val setEntryReadDao: SetEntryReadDao,
    private val setEntryWriteDao: SetEntryWriteDao
) {
    fun readAllSetEntryData(): Flow<List<SetEntry>> = setEntryReadDao.readAllSetEntryData()

    suspend fun addSetEntryReturningId(setEntry: SetEntry): Long {
        return setEntryWriteDao.addSetEntry(setEntry)
    }
}
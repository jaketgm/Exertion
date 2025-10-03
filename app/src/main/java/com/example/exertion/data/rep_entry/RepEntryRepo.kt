package com.example.exertion.data.rep_entry

import androidx.lifecycle.LiveData
import com.example.exertion.data.rep_entry.read_dao.RepEntryReadDao
import com.example.exertion.data.rep_entry.write_dao.RepEntryWriteDao
import kotlinx.coroutines.flow.Flow

class RepEntryRepo(
    private val repEntryReadDao: RepEntryReadDao,
    private val repEntryWriteDao: RepEntryWriteDao
) {
    fun readAllRepEntryData(): Flow<List<RepEntry>> = repEntryReadDao.readAllRepEntryData()

    suspend fun addRepEntry(repEntry: RepEntry) {
        repEntryWriteDao.addRepEntry(repEntry)
    }
}
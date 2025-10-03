package com.example.exertion.data.set_entry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exertion.data.ExertionDB
import kotlinx.coroutines.flow.Flow

class SetEntryVM(application: Application): AndroidViewModel(application) {
    private val read_all_setEntry_data: Flow<List<SetEntry>>
    private val set_entry_repository: SetEntryRepo

    init {
        val db = ExertionDB.getDatabase(application)
        val setEntryReadDao = db.setEntryReadDao()
        val setEntryWriteDao = db.setEntryWriteDao()
        set_entry_repository = SetEntryRepo(setEntryReadDao, setEntryWriteDao)
        read_all_setEntry_data = set_entry_repository.readAllSetEntryData()
    }

    suspend fun addSetEntry(setEntry: SetEntry) {
        set_entry_repository.addSetEntry(setEntry)
    }
}
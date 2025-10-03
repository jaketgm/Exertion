package com.example.exertion.data.set_entry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.exertion.data.EXERTION_DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SetEntryVM(application: Application): AndroidViewModel(application) {
    private val read_all_setEntry_data: Flow<List<SetEntry>>
    private val set_entry_repository: SetEntryRepo

    init {
        val db = EXERTION_DB.getDatabase(application)
        val setEntryReadDao = db.setEntryReadDao()
        val setEntryWriteDao = db.setEntryWriteDao()
        set_entry_repository = SetEntryRepo(setEntryReadDao, setEntryWriteDao)
        read_all_setEntry_data = set_entry_repository.readAllSetEntryData()
    }

    suspend fun addSetEntry(setEntry: SetEntry) {
        set_entry_repository.addSetEntry(setEntry)
    }
}
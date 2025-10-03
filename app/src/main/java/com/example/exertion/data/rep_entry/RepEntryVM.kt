package com.example.exertion.data.rep_entry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.exertion.data.EXERTION_DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RepEntryVM(application: Application): AndroidViewModel(application) {
    private val read_all_repEntry_data: Flow<List<RepEntry>>
    private val rep_entry_repository: RepEntryRepo

    init {
        val db = EXERTION_DB.getDatabase(application)
        val repEntryReadDao = db.repEntryReadDao()
        val repEntryWriteDao = db.repEntryWriteDao()
        rep_entry_repository = RepEntryRepo(repEntryReadDao, repEntryWriteDao)
        read_all_repEntry_data = rep_entry_repository.readAllRepEntryData()
    }

    suspend fun addRepEntry(repEntry: RepEntry) {
        rep_entry_repository.addRepEntry(repEntry)
    }
}
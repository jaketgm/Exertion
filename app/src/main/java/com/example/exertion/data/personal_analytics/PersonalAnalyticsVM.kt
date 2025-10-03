package com.example.exertion.data.personal_analytics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exertion.data.ExertionDB
import kotlinx.coroutines.flow.Flow

class PersonalAnalyticsVM(application: Application): AndroidViewModel(application) {
    private val read_all_data_pa: Flow<List<PersonalAnalytics>>
    private val repository_pa: PersonalAnalyticsRepo

    init {
        val db = ExertionDB.getDatabase(application)
        val personalAnalyticsReadDao = db.personalAnalyticsReadDao()
        val personalAnalyticsWriteDao = db.personalAnalyticsWriteDao()
        repository_pa = PersonalAnalyticsRepo(personalAnalyticsReadDao, personalAnalyticsWriteDao)
        read_all_data_pa = repository_pa.readAllDataPA()
    }

    suspend fun addPersonalAnalytics(personalAnalytics: PersonalAnalytics) {
        repository_pa.addPersonalAnalytics(personalAnalytics)
    }
}
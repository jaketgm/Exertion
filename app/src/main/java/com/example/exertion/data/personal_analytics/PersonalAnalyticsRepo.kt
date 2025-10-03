package com.example.exertion.data.personal_analytics

import androidx.lifecycle.LiveData
import com.example.exertion.data.personal_analytics.read_dao.PersonalAnalyticsReadDao
import com.example.exertion.data.personal_analytics.write_dao.PersonalAnalyticsWriteDao
import kotlinx.coroutines.flow.Flow

class PersonalAnalyticsRepo(
    private val personalAnalyticsReadDao: PersonalAnalyticsReadDao,
    private val personalAnalyticsWriteDao: PersonalAnalyticsWriteDao
) {
    fun readAllDataPA(): Flow<List<PersonalAnalytics>> = personalAnalyticsReadDao.readAllDataPA()

    suspend fun addPersonalAnalytics(personalAnalytics: PersonalAnalytics) {
        personalAnalyticsWriteDao.addPersonalAnalytics(personalAnalytics)
    }
}
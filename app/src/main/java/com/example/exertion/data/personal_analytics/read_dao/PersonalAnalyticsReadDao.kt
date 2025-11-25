package com.example.exertion.data.personal_analytics.read_dao

import androidx.room.Dao
import androidx.room.Query
import com.example.exertion.data.personal_analytics.PersonalAnalytics
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalAnalyticsReadDao {
    @Query(
        "SELECT * FROM PERSONAL_ANALYTICS ORDER BY entry_id ASC"
    )
    fun readAllDataPA(): Flow<List<PersonalAnalytics>>
}
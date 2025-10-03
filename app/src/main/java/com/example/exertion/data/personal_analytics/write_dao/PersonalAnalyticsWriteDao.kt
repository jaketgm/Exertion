package com.example.exertion.data.personal_analytics.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.exertion.data.personal_analytics.PersonalAnalytics

@Dao
interface PersonalAnalyticsWriteDao {
    @Insert(onConflict = OnConflictStrategy.Companion.ABORT)
    suspend fun addPersonalAnalytics(personalAnalytics: PersonalAnalytics)
}
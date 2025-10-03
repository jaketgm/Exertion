package com.example.exertion.data.daily_user_metric_snapshot.write_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.exertion.data.daily_user_metric_snapshot.DailyUserMetricSnapshot

@Dao
interface DailyUserMetricSnapshotWriteDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addDailyUserMetricSnapshot(dailyUserMetricSnapshot: DailyUserMetricSnapshot)
}
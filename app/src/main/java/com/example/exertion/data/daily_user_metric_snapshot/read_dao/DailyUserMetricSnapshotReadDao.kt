package com.example.exertion.data.daily_user_metric_snapshot.read_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exertion.data.daily_user_metric_snapshot.DailyUserMetricSnapshot
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyUserMetricSnapshotReadDao {
    @Query(
        "SELECT * FROM DAILY_USER_METRIC_SNAPSHOT ORDER BY snapshot_id ASC"
    )
    fun readAllDailyUserMetricSnapshotData(): Flow<List<DailyUserMetricSnapshot>>
}
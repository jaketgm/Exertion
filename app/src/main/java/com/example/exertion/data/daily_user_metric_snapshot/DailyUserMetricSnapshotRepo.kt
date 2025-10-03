package com.example.exertion.data.daily_user_metric_snapshot

import androidx.lifecycle.LiveData
import com.example.exertion.data.daily_user_metric_snapshot.read_dao.DailyUserMetricSnapshotReadDao
import com.example.exertion.data.daily_user_metric_snapshot.write_dao.DailyUserMetricSnapshotWriteDao
import kotlinx.coroutines.flow.Flow

class DailyUserMetricSnapshotRepo(
    private val dailyUserMetricSnapshotReadDao: DailyUserMetricSnapshotReadDao,
    private val dailyUserMetricSnapshotWriteDao: DailyUserMetricSnapshotWriteDao
) {
    fun readAllDailyUserMetricSnapshotData(): Flow<List<DailyUserMetricSnapshot>> = dailyUserMetricSnapshotReadDao.readAllDailyUserMetricSnapshotData()

    suspend fun addDailyUserMetricSnapshot(dailyUserMetricSnapshot: DailyUserMetricSnapshot) {
        dailyUserMetricSnapshotWriteDao.addDailyUserMetricSnapshot(dailyUserMetricSnapshot)
    }
}
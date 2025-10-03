package com.example.exertion.data.daily_user_metric_snapshot

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exertion.data.ExertionDB
import kotlinx.coroutines.flow.Flow

class DailyUserMetricSnapshotVM(application: Application): AndroidViewModel(application) {
    private val read_all_dailyUserMetricSnapshot_data: Flow<List<DailyUserMetricSnapshot>>
    private val daily_user_metric_snapshot_repository: DailyUserMetricSnapshotRepo

    init {
        val db = ExertionDB.getDatabase(application)
        val dailyUserMetricSnapshotReadDao = db.dailyUserMetricSnapshotReadDao()
        val dailyUserMetricSnapshotWriteDao = db.dailyUserMetricSnapshotWriteDao()
        daily_user_metric_snapshot_repository = DailyUserMetricSnapshotRepo(dailyUserMetricSnapshotReadDao, dailyUserMetricSnapshotWriteDao)
        read_all_dailyUserMetricSnapshot_data = daily_user_metric_snapshot_repository.readAllDailyUserMetricSnapshotData()
    }
}
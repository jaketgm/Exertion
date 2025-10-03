package com.example.exertion.data.daily_user_metric_snapshot

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.exertion.data.EXERTION_DB
import kotlinx.coroutines.flow.Flow

class DailyUserMetricSnapshotVM(application: Application): AndroidViewModel(application) {
    private val read_all_dailyUserMetricSnapshot_data: Flow<List<DailyUserMetricSnapshot>>
    private val daily_user_metric_snapshot_repository: DailyUserMetricSnapshotRepo

    init {
        val db = EXERTION_DB.getDatabase(application)
        val dailyUserMetricSnapshotReadDao = db.dailyUserMetricSnapshotReadDao()
        val dailyUserMetricSnapshotWriteDao = db.dailyUserMetricSnapshotWriteDao()
        daily_user_metric_snapshot_repository = DailyUserMetricSnapshotRepo(dailyUserMetricSnapshotReadDao, dailyUserMetricSnapshotWriteDao)
        read_all_dailyUserMetricSnapshot_data = daily_user_metric_snapshot_repository.readAllDailyUserMetricSnapshotData()
    }
}
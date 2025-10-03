package com.example.exertion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.exertion.data.daily_user_metric_snapshot.read_dao.DailyUserMetricSnapshotReadDao
import com.example.exertion.data.daily_user_metric_snapshot.write_dao.DailyUserMetricSnapshotWriteDao
import com.example.exertion.data.exercise.read_dao.ExerciseReadDao
import com.example.exertion.data.exercise.write_dao.ExerciseWriteDao
import com.example.exertion.data.exercise_metric_snapshot.read_dao.ExerciseMetricSnapshotReadDao
import com.example.exertion.data.exercise_metric_snapshot.write_dao.ExerciseMetricSnapshotWriteDao
import com.example.exertion.data.personal_analytics.PersonalAnalytics
import com.example.exertion.data.personal_analytics.read_dao.PersonalAnalyticsReadDao
import com.example.exertion.data.personal_analytics.write_dao.PersonalAnalyticsWriteDao
import com.example.exertion.data.rep_entry.read_dao.RepEntryReadDao
import com.example.exertion.data.rep_entry.write_dao.RepEntryWriteDao
import com.example.exertion.data.set_entry.read_dao.SetEntryReadDao
import com.example.exertion.data.set_entry.write_dao.SetEntryWriteDao
import com.example.exertion.data.user_table.read_dao.UserReadDao
import com.example.exertion.data.user_table.UserTable
import com.example.exertion.data.user_table.write_dao.UserWriteDao
import com.example.exertion.data.workout.WORKOUT_DAO
import com.example.exertion.data.workout_exercise.WORKOUT_EXERCISE_DAO
import com.example.exertion.data.workout_metric_snapshot.WORKOUT_METRIC_SNAPSHOT_DAO

@Database(
    entities = [UserTable::class, PersonalAnalytics::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(CONVERTERS::class)
abstract class EXERTION_DB: RoomDatabase() {
    abstract fun userReadDao(): UserReadDao
    abstract fun userWriteDao(): UserWriteDao
    abstract fun personalAnalyticsReadDao(): PersonalAnalyticsReadDao
    abstract fun personalAnalyticsWriteDao(): PersonalAnalyticsWriteDao
    abstract fun exerciseReadDao(): ExerciseReadDao
    abstract fun exerciseWriteDao(): ExerciseWriteDao
    abstract fun workoutDao(): WORKOUT_DAO
    abstract fun workoutExerciseDao(): WORKOUT_EXERCISE_DAO
    abstract fun setEntryReadDao(): SetEntryReadDao
    abstract fun setEntryWriteDao(): SetEntryWriteDao
    abstract fun repEntryReadDao(): RepEntryReadDao
    abstract fun repEntryWriteDao(): RepEntryWriteDao
    abstract fun workoutMetricSnapshotDao(): WORKOUT_METRIC_SNAPSHOT_DAO
    abstract fun dailyUserMetricSnapshotReadDao(): DailyUserMetricSnapshotReadDao
    abstract fun dailyUserMetricSnapshotWriteDao(): DailyUserMetricSnapshotWriteDao
    abstract fun exerciseMetricSnapshotReadDao(): ExerciseMetricSnapshotReadDao
    abstract fun exerciseMetricSnapshotWriteDao(): ExerciseMetricSnapshotWriteDao

    companion object {
        // writes are visible to other threads
        @Volatile
        private var INSTANCE: EXERTION_DB? = null // singleton class

        fun getDatabase(context: Context): EXERTION_DB {
            val temp_instance = INSTANCE
            if (temp_instance != null) {
                return temp_instance
            }
            // protected from concurrent execution by multiple threads
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EXERTION_DB::class.java,
                    "exertion_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
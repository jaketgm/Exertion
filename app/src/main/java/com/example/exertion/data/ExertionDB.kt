package com.example.exertion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.exertion.data.daily_user_metric_snapshot.DailyUserMetricSnapshot
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
import com.example.exertion.data.exercise.ExerciseTable
import com.example.exertion.data.exercise_metric_snapshot.ExerciseMetricSnapshot
import com.example.exertion.data.rep_entry.RepEntry
import com.example.exertion.data.set_entry.SetEntry
import com.example.exertion.data.user_table.write_dao.UserWriteDao
import com.example.exertion.data.workout.Workout
import com.example.exertion.data.workout.read_dao.WorkoutReadDao
import com.example.exertion.data.workout.write_dao.WorkoutWriteDao
import com.example.exertion.data.workout_exercise.WorkoutExercise
import com.example.exertion.data.workout_exercise.read_dao.WorkoutExerciseReadDao
import com.example.exertion.data.workout_exercise.write_dao.WorkoutExerciseWriteDao
import com.example.exertion.data.workout_metric_snapshot.WorkoutMetricSnapshot
import com.example.exertion.data.workout_metric_snapshot.read_dao.WorkoutMetricSnapshotReadDao
import com.example.exertion.data.workout_metric_snapshot.write_dao.WorkoutMetricSnapshotWriteDao

@Database(
    entities = [
        UserTable::class,
        PersonalAnalytics::class,
        DailyUserMetricSnapshot::class,
        ExerciseTable::class,
        ExerciseMetricSnapshot::class,
        RepEntry::class,
        SetEntry::class,
        Workout::class,
        WorkoutExercise::class,
        WorkoutMetricSnapshot::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ExertionDB : RoomDatabase() {

    abstract fun userReadDao(): UserReadDao
    abstract fun userWriteDao(): UserWriteDao
    abstract fun personalAnalyticsReadDao(): PersonalAnalyticsReadDao
    abstract fun personalAnalyticsWriteDao(): PersonalAnalyticsWriteDao
    abstract fun exerciseReadDao(): ExerciseReadDao
    abstract fun exerciseWriteDao(): ExerciseWriteDao
    abstract fun workoutReadDao(): WorkoutReadDao
    abstract fun workoutWriteDao(): WorkoutWriteDao
    abstract fun workoutExerciseReadDao(): WorkoutExerciseReadDao
    abstract fun workoutExerciseWriteDao(): WorkoutExerciseWriteDao
    abstract fun setEntryReadDao(): SetEntryReadDao
    abstract fun setEntryWriteDao(): SetEntryWriteDao
    abstract fun repEntryReadDao(): RepEntryReadDao
    abstract fun repEntryWriteDao(): RepEntryWriteDao
    abstract fun workoutMetricSnapshotReadDao(): WorkoutMetricSnapshotReadDao
    abstract fun workoutMetricSnapshotWriteDao(): WorkoutMetricSnapshotWriteDao
    abstract fun dailyUserMetricSnapshotReadDao(): DailyUserMetricSnapshotReadDao
    abstract fun dailyUserMetricSnapshotWriteDao(): DailyUserMetricSnapshotWriteDao
    abstract fun exerciseMetricSnapshotReadDao(): ExerciseMetricSnapshotReadDao
    abstract fun exerciseMetricSnapshotWriteDao(): ExerciseMetricSnapshotWriteDao

    companion object {
        @Volatile
        private var INSTANCE: ExertionDB? = null

        fun getDatabase(context: Context): ExertionDB {
            val tmp = INSTANCE
            if (tmp != null) return tmp

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExertionDB::class.java,
                    "exertion_database"
                )
                    // Fine while you’re iterating on the schema
                    .fallbackToDestructiveMigration(true)
                    .addCallback(object : RoomDatabase.Callback() {

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            seedExerciseTable(db)
                        }

                        private fun seedExerciseTable(db: SupportSQLiteDatabase) {
                            // Your entity is tableName = "exercise"
                            // Columns:
                            // exercise_id (PK, NOT NULL, AUTOINCR)
                            // name (TEXT NOT NULL)
                            // muscle_group (TEXT NOT NULL)
                            // equipment (TEXT, NULL)
                            // is_unilateral (INTEGER NOT NULL DEFAULT 0)
                            // is_custom (INTEGER NOT NULL DEFAULT 0)
                            // user_id (INTEGER, NULL, FK → user_table.user_id, but NULL is allowed)
                            // notes (TEXT, NULL)

                            db.execSQL(
                                """
                                INSERT INTO exercise (
                                    exercise_id,
                                    name,
                                    muscle_group,
                                    equipment,
                                    is_unilateral,
                                    is_custom,
                                    user_id,
                                    notes
                                ) VALUES
                                (1, 'Bench Press', 'Chest', 'Barbell', 0, 0, NULL, NULL),
                                (2, 'Squat', 'Quads', 'Barbell', 0, 0, NULL, NULL),
                                (3, 'Deadlift', 'Hamstrings', 'Barbell', 0, 0, NULL, NULL)
                                """.trimIndent()
                            )
                        }
                    })
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
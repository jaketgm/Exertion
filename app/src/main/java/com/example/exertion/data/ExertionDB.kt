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
    entities = [UserTable::class, PersonalAnalytics::class, DailyUserMetricSnapshot::class, ExerciseTable::class, ExerciseMetricSnapshot::class, RepEntry::class, SetEntry::class, Workout::class, WorkoutExercise::class, WorkoutMetricSnapshot::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ExertionDB: RoomDatabase() {
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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {

                // --------------------------------------------------
                // 1. WORKOUT (add day_of_week)
                // --------------------------------------------------
                db.execSQL("""
            CREATE TABLE workout_new (
                workout_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                user_id INTEGER NOT NULL,
                name TEXT,
                started_at INTEGER NOT NULL,
                ended_at INTEGER,
                kind TEXT NOT NULL,
                mesocycle_name TEXT,
                mesocycle_week INTEGER,
                notes TEXT,
                day_of_week INTEGER NOT NULL DEFAULT 1,
                FOREIGN KEY(user_id) REFERENCES user_table(user_id) ON DELETE CASCADE
            )
        """.trimIndent())

                db.execSQL("""
            INSERT INTO workout_new (
                workout_id, user_id, name, started_at, ended_at, kind,
                mesocycle_name, mesocycle_week, notes, day_of_week
            )
            SELECT workout_id, user_id, name, started_at, ended_at, kind,
                   mesocycle_name, mesocycle_week, notes, 1
            FROM workout
        """.trimIndent())

                db.execSQL("DROP TABLE workout")
                db.execSQL("ALTER TABLE workout_new RENAME TO workout")

                db.execSQL("""
            CREATE UNIQUE INDEX index_workout_user_id_started_at
            ON workout(user_id, started_at)
        """.trimIndent())


                // --------------------------------------------------
                // 2. WORKOUT_EXERCISE (major changes: new FKs + indexes)
                // --------------------------------------------------
                db.execSQL("""
            CREATE TABLE workout_exercise_new (
                workout_exercise_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                workout_id INTEGER NOT NULL,
                exercise_id INTEGER NOT NULL,
                exercise_order INTEGER NOT NULL,
                target_sets INTEGER,
                target_reps INTEGER,
                target_weight REAL,
                FOREIGN KEY(workout_id) REFERENCES workout(workout_id) ON DELETE CASCADE,
                FOREIGN KEY(exercise_id) REFERENCES exercise_table(exercise_id) ON DELETE RESTRICT
            )
        """.trimIndent())

                db.execSQL("""
            INSERT INTO workout_exercise_new (
                workout_exercise_id, workout_id, exercise_id,
                exercise_order, target_sets, target_reps, target_weight
            )
            SELECT workout_exercise_id, workout_id, exercise_id,
                   exercise_order, target_sets, target_reps, target_weight
            FROM workout_exercise
        """.trimIndent())

                db.execSQL("DROP TABLE workout_exercise")
                db.execSQL("ALTER TABLE workout_exercise_new RENAME TO workout_exercise")

                db.execSQL("""
            CREATE UNIQUE INDEX index_workout_exercise_workout_id_exercise_order
            ON workout_exercise(workout_id, exercise_order)
        """.trimIndent())

                db.execSQL("""
            CREATE INDEX index_workout_exercise_workout_id
            ON workout_exercise(workout_id)
        """.trimIndent())

                db.execSQL("""
            CREATE INDEX index_workout_exercise_exercise_id
            ON workout_exercise(exercise_id)
        """.trimIndent())


                // --------------------------------------------------
                // 3. SET_ENTRY (massively changed — must rebuild)
                // --------------------------------------------------
                db.execSQL("""
            CREATE TABLE set_entry_new (
                set_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                workout_exercise_id INTEGER NOT NULL,
                set_index INTEGER NOT NULL,
                set_type TEXT,
                reps INTEGER NOT NULL,
                weight_kg REAL,
                tut_ms REAL,
                rir REAL,
                rpe REAL,
                rest_sec INTEGER,
                is_warmup INTEGER NOT NULL DEFAULT 0,
                is_failure INTEGER NOT NULL DEFAULT 0,
                timestamp INTEGER NOT NULL,
                top REAL NOT NULL DEFAULT 0.0,
                bottom REAL NOT NULL DEFAULT 0.0,
                tempo_notation TEXT,
                velocity_loss_pct REAL,
                rir_suggested REAL,
                rpe_suggested REAL,
                suggestion_confidence REAL,
                FOREIGN KEY(workout_exercise_id)
                    REFERENCES workout_exercise(workout_exercise_id)
                    ON DELETE CASCADE
            )
        """.trimIndent())

                db.execSQL("""
            INSERT INTO set_entry_new (
                set_id, workout_exercise_id, set_index, set_type,
                reps, weight_kg, tut_ms, rir, rpe,
                rest_sec, is_warmup, is_failure, timestamp,
                top, bottom, tempo_notation, velocity_loss_pct,
                rir_suggested, rpe_suggested, suggestion_confidence
            )
            SELECT set_id, workout_exercise_id, set_index, set_type,
                   reps, weight_kg, tut_ms, rir, rpe,
                   rest_sec, is_warmup, is_failure, timestamp,
                   top, bottom, tempo_notation, velocity_loss_pct,
                   rir_suggested, rpe_suggested, suggestion_confidence
            FROM set_entry
        """.trimIndent())

                db.execSQL("DROP TABLE set_entry")
                db.execSQL("ALTER TABLE set_entry_new RENAME TO set_entry")

                db.execSQL("""
            CREATE UNIQUE INDEX index_set_entry_workout_exercise_id_set_index
            ON set_entry(workout_exercise_id, set_index)
        """.trimIndent())

                db.execSQL("""
            CREATE INDEX index_set_entry_workout_exercise_id
            ON set_entry(workout_exercise_id)
        """.trimIndent())
            }
        }

        fun getDatabase(context: Context): ExertionDB {
            val temp_instance = INSTANCE
            if (temp_instance != null) {
                return temp_instance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExertionDB::class.java,
                    "exertion_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .addMigrations(MIGRATION_2_3)
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
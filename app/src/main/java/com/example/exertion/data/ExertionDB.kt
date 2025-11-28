package com.example.exertion.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.exertion.data.seed.SeedData

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
                    .fallbackToDestructiveMigration(true)
                    .addCallback(object : RoomDatabase.Callback() {

                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {

                                val instance = INSTANCE ?: return@launch

                                val userDao = instance.userWriteDao()
                                val exerciseDao = instance.exerciseWriteDao()
                                val workoutDao = instance.workoutWriteDao()
                                val workoutExerciseDao = instance.workoutExerciseWriteDao()
                                val setDao = instance.setEntryWriteDao()
                                val repDao = instance.repEntryWriteDao()
                                val dailyDao = instance.dailyUserMetricSnapshotWriteDao()
                                val exerciseSnapshotDao = instance.exerciseMetricSnapshotWriteDao()
                                val personalDao = instance.personalAnalyticsWriteDao()

                                val user = SeedData.user
                                val user2 = SeedData.user2

                                val exercises = SeedData.exercises
                                val exercises_for_user2 = SeedData.exercises_for_user2

                                val workout1 = SeedData.workout1
                                val workout_for_user2 = SeedData.workout_for_user2

                                val workoutExercises = SeedData.workoutExercises
                                val workout_exercises_for_user2 = SeedData.workout_exercises_for_user2

                                val sets = SeedData.sets
                                val sets_for_user2 = SeedData.sets_for_user2

                                val reps = SeedData.reps
                                val reps_for_user2 = SeedData.reps_for_user2

                                val daily = SeedData.daily
                                val daily_for_user2 = SeedData.daily_for_user2

                                val exerciseSnapshots = SeedData.exerciseSnapshots
                                val exercise_snapshots_for_user2 = SeedData.exercise_snapshots_for_user2

                                val analytics = SeedData.analytics
                                val analytics_for_user2 = SeedData.analytics_for_user2

                                userDao.upsertUser(user)
                                userDao.upsertUser(user2)

                                exerciseDao.insertExercises(exercises)
                                exerciseDao.insertExercises(exercises_for_user2)

                                workoutDao.addWorkout(workout1)
                                workoutDao.addWorkout(workout_for_user2)

                                workoutExerciseDao.addWorkoutExercise(workoutExercises)
                                workoutExerciseDao.addWorkoutExercise(workout_exercises_for_user2)

                                setDao.addSetEntries(sets)
                                setDao.addSetEntries(sets_for_user2)

                                repDao.addRepEntry(reps)
                                repDao.addRepEntry(reps_for_user2)

                                dailyDao.addDailyUserMetricSnapshot(daily)
                                dailyDao.addDailyUserMetricSnapshot(daily_for_user2)

                                exerciseSnapshotDao.addExerciseMetricSnapshot(exerciseSnapshots)
                                exerciseSnapshotDao.addExerciseMetricSnapshot(exercise_snapshots_for_user2)

                                personalDao.addPersonalAnalytics(analytics)
                                personalDao.addPersonalAnalytics(analytics_for_user2)
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
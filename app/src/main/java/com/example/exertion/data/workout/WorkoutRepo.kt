package com.example.exertion.data.workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.exertion.data.db.relations.WorkoutWithExercises
import com.example.exertion.data.workout.read_dao.WorkoutReadDao
import com.example.exertion.data.workout.write_dao.WorkoutWriteDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class WorkoutRepo(
    private val workoutReadDao: WorkoutReadDao,
    private val workoutWriteDao: WorkoutWriteDao
) {
    fun readAllWorkoutData(): Flow<List<Workout>> = workoutReadDao.readAllWorkoutData()

    suspend fun addWorkout(workout: Workout) {
        workoutWriteDao.addWorkout(workout)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getOrCreateWorkoutForToday(userId: Int): Int {
        val now = System.currentTimeMillis()

        // 1. Check if workout already exists
        val existing = workoutReadDao.getWorkoutForToday(
            userId = userId,
            todayEpoch = now
        )
        if (existing != null) return existing.workout_id

        // 2. Create new workout
        val newWorkout = Workout(
            workout_id = 0,
            user_id = userId,
            name = "Today's Workout",
            started_at = now,
            ended_at = null,
            kind = "session",
            mesocycle_name = null,
            mesocycle_week = null,
            notes = null,
            day_of_week = LocalDate.now().dayOfWeek.value // 1 = Monday … 7 = Sunday
        )

        return workoutWriteDao.addWorkout(newWorkout).toInt()
    }
}
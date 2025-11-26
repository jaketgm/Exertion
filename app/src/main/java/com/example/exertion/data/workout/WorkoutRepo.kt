package com.example.exertion.data.workout

import android.os.Build
import androidx.annotation.RequiresApi
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
        // if any workout already exists for this user, treat it as today's
        val first = workoutReadDao.getFirstWorkoutForUser(userId)
        if (first != null) return first.workout_id

        // otherwise, create a new workout
        val now = System.currentTimeMillis()
        val today = LocalDate.now().dayOfWeek.value

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
            day_of_week = today
        )

        return workoutWriteDao.addWorkout(newWorkout).toInt()
    }
}
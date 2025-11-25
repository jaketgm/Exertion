package com.example.exertion.data.seed

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.exertion.data.Gender
import com.example.exertion.data.daily_user_metric_snapshot.DailyUserMetricSnapshot
import com.example.exertion.data.exercise.ExerciseTable
import com.example.exertion.data.exercise_metric_snapshot.ExerciseMetricSnapshot
import com.example.exertion.data.personal_analytics.PersonalAnalytics
import com.example.exertion.data.rep_entry.RepEntry
import com.example.exertion.data.set_entry.SetEntry
import com.example.exertion.data.user_table.UserTable
import com.example.exertion.data.workout.Workout
import com.example.exertion.data.workout_exercise.WorkoutExercise
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.time.ExperimentalTime
import java.time.Instant
import java.time.temporal.TemporalAdjusters

@RequiresApi(Build.VERSION_CODES.O)
fun weekStart(): Long {
    val today = LocalDate.now(ZoneOffset.UTC)
    val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    return monday.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}

@RequiresApi(Build.VERSION_CODES.O)
fun weekEnd(): Long {
    val today = LocalDate.now(ZoneOffset.UTC)
    val sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    return sunday.atTime(23, 59, 59, 999_000_000)
        .atZone(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
}

object SeedData {
    val user = UserTable(
        user_id = 1,
        username = "jaketgm",
        email = "jakeruns7@gmail.com",
        password_hash = "Pewds2017",
        created_at = System.currentTimeMillis(),
        age = 20,
        weight_kg = 70.0,
        height_cm = 180.0,
        gender = "male"
    )

    val exercises = listOf(
        ExerciseTable(1, "Barbell Bench Press", "Chest", "Barbell", false, false, 1, null),
        ExerciseTable(2, "Back Squat", "Legs", "Barbell", false, false, 1, null),
        ExerciseTable(3, "Conventional Deadlift", "Back", "Barbell", false, false, 1, null),
        ExerciseTable(4, "Overhead Press", "Shoulders", "Barbell", false, false, 1, null),
        ExerciseTable(5, "Lat Pulldown", "Back", "Machine", false, false, 1, null),
        ExerciseTable(6, "Dumbbell Curl", "Arms", "Dumbbells", true, false, 1, null)
    )

    val workout1 = Workout(
        workout_id = 1,
        user_id = 1,
        name = "Upper Body – Bench Focus",
        started_at = 1732406400000L,
        ended_at = 1732410000000L,
        mesocycle_name = "Hypertrophy Phase",
        mesocycle_week = 2,
        notes = "Solid session, velocity felt good",
        day_of_week = 1
    )

    val workoutExercises = listOf(
        WorkoutExercise(1, 1, 1, 1, 4, 6, 80.0), // Bench Press
        WorkoutExercise(2, 1, 4, 2, 3, 8, 45.0), // OHP
        WorkoutExercise(3, 1, 6, 3, 3, 12, 15.0) // Dumbbell Curl
    )

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalTime::class)
    val sets = listOf(
        SetEntry(
            1,
            1,
            1,
            "working",
            6,
            80.0,
            3000.0,
            2.0,
            8.5,
            180,
            false,
            false,
            Instant.now(),
            0.28,
            0.12,
            "3-0-1-0",
            12.0,
            1.5,
            8.5,
            0.92
        ),
        SetEntry(
            2,
            1,
            2, "working", 6,
            80.0,
            3100.0,
            1.0,
            9.0,
            180,
            false,
            true,
            Instant.now(),
            0.26,
            0.11,
            "3-0-1-0",
            17.0,
            2.0,
            9.0,
            0.88
        ),
        SetEntry(
            3,
            2,
            1,
            "working",
            8,
            45.0,
            3200.0,
            3.0,
            7.5,
            150,
            false,
            false,
            Instant.now(),
            0.45,
            0.19,
            "2-0-1-0",
            9.0,
            1.0,
            7.5,
            0.93
        ),
        SetEntry(
            4,
            2,
            2,
            "working",
            8,
            45.0,
            3300.0,
            2.0,
            8.0,
            150,
            false,
            false,
            Instant.now(),
            0.43,
            0.18,
            "2-0-1-0",
            11.0,
            1.2,
            8.0,
            0.90
        ),
        SetEntry(
            5,
            3,
            1,
            "working",
            12,
            15.0,
            3500.0,
            4.0,
            7.0,
            120,
            false,
            false,
            Instant.now(),
            0.62,
            0.30,
            "1-0-3-0",
            5.0,
            0.7,
            7.0,
            0.88
        ),
        SetEntry(
            6,
            3,
            2,
            "working",
            12,
            15.0,
            3600.0,
            3.0,
            7.5,
            120,
            false,
            false,
            Instant.now(),
            0.60,
            0.29,
            "1-0-3-0",
            6.0,
            0.8,
            7.5,
            0.90
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalTime::class)
    val reps = listOf(
        RepEntry(1, 1, 1, 900.0, 1400.0, 2300.0, 0.35, 85.0, true, Instant.now()),
        RepEntry(2, 1, 2, 950.0, 1350.0, 2300.0, 0.33, 84.0, true, Instant.now()),
        RepEntry(3, 1, 3, 1000.0, 1300.0, 2300.0, 0.31, 83.0, true, Instant.now())
    )

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalTime::class)
    val daily = DailyUserMetricSnapshot(
        user_id = 1,
        date_utc = 20241124,
        total_volume_kg = 80 * 6 + 80 * 6 + 45 * 8 + 45 * 8 + 15 * 12 + 15 * 12,
        total_sets = 6,
        total_reps = 6 + 6 + 8 + 8 + 12 + 12,
        total_tut_ms = 3000.0 + 3100.0 + 3200.0 + 3300.0 + 3500.0 + 3600.0,
        avg_velocity_mps = 0.35,
        est_1rm_best_kg = 100.0,
        dots = 320.0,
        computed_at = Instant.now()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalTime::class)
    val exerciseSnapshots = listOf(
        ExerciseMetricSnapshot(
            user_id = 1,
            exercise_id = 1,
            period_start = weekStart(),
            period_end = weekEnd(),
            total_volume_kg = 80.0 * 12,
            top_est_1rm_kg = 102.5,
            avg_tut_ms = 3050.0,
            sessions = 1,
            computed_at = Instant.now()
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalTime::class)
    val analytics = PersonalAnalytics(
        user_id = 1,
        measured_at = Instant.now(),
        weight_kg = 70.0,
        height_cm = 180.0,
        bodyfat_pct = 10.0,
        maintenance_calorie = 2800.0,
        age = 20,
        gender = Gender.MALE
    )
}
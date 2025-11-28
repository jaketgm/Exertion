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

    val user2 = UserTable(
        user_id = 2,
        username = "alexfit",
        email = "alexfit@email.com",
        password_hash = "4kHt5[4|Fml_",
        created_at = System.currentTimeMillis(),
        age = 27,
        weight_kg = 82.5,
        height_cm = 178.0,
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

    val exercises_for_user2 = listOf(
        ExerciseTable(10, "Back Squat", "Legs", "Barbell", false, false, 2, null),
        ExerciseTable(11, "Romanian Deadlift", "Hamstrings", "Barbell", false, false, 2, null),
        ExerciseTable(12, "Leg Press", "Legs", "Machine", false, false, 2, null),
        ExerciseTable(13, "Glute Bridge", "Glutes", "Barbell", false, false, 2, null),
        ExerciseTable(14, "Calf Raise", "Calves", "Machine", false, false, 2, null)
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

    val workout_for_user2 = Workout(
        workout_id = 2,
        user_id = 2,
        name = "Lower Body – Strength Session",
        started_at = 1732492800000L,
        ended_at = 1732496400000L,
        mesocycle_name = "Strength Phase",
        mesocycle_week = 1,
        notes = "Good session, heavy squats felt solid",
        day_of_week = 3
    )

    val workoutExercises = listOf(
        WorkoutExercise(1, 1, 1, 1, 4, 6, 80.0), // Bench Press
        WorkoutExercise(2, 1, 4, 2, 3, 8, 45.0), // OHP
        WorkoutExercise(3, 1, 6, 3, 3, 12, 15.0) // Dumbbell Curl
    )

    val workout_exercises_for_user2 = listOf(
        WorkoutExercise(10, 2, 10, 1, 5, 5, 140.0), // Back Squat
        WorkoutExercise(11, 2, 11, 2, 4, 8, 100.0), // RDL
        WorkoutExercise(12, 2, 12, 3, 3, 12, 300.0), // Leg Press
        WorkoutExercise(13, 2, 14, 4, 4, 12, 60.0)   // Calf Raise
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
    val sets_for_user2 = listOf(
        // Back Squat
        SetEntry(
            20,
            10,
            1,
            "working",
            5,
            140.0,
            3200.0,
            1.5,
            8.0,
            240,
            false,
            false,
            Instant.now(),
            0.38,
            0.15,
            "2-1-1-0",
            10.0,
            1.0,
            8.0,
            0.90
        ),
        SetEntry(
            21,
            10,
            2,
            "working",
            5,
            140.0,
            3300.0,
            2.0,
            8.5,
            240,
            false,
            false,
            Instant.now(),
            0.35,
            0.14,
            "2-1-1-0",
            12.0,
            1.2,
            8.5,
            0.88
        ),
        SetEntry(
            22,
            11,
            1,
            "working",
            8,
            100.0,
            3400.0,
            3.0,
            7.5,
            180,
            false,
            false,
            Instant.now(),
            0.55,
            0.22,
            "3-0-2-0",
            6.0,
            0.5,
            7.5,
            0.92
        ),
        SetEntry(
            23,
            12,
            1,
            "working",
            12,
            300.0,
            4500.0,
            4.0,
            7.0,
            180,
            false,
            false,
            Instant.now(),
            0.70,
            0.25,
            "1-0-2-0",
            5.0,
            0.5,
            7.0,
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
    val reps_for_user2 = listOf(
        // Squat set 1
        RepEntry(20, 20, 1, 850.0, 1400.0, 2250.0, 0.33, 90.0, true, Instant.now()),
        RepEntry(21, 20, 2, 900.0, 1350.0, 2250.0, 0.32, 92.0, true, Instant.now()),
        RepEntry(22, 20, 3, 950.0, 1300.0, 2250.0, 0.30, 89.0, true, Instant.now())
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
    val daily_for_user2 = DailyUserMetricSnapshot(
        user_id = 2,
        date_utc = 20241125, // day after user 1 session
        total_volume_kg = 140*5 + 140*5 + 100*8 + 300*12,
        total_sets = 4,
        total_reps = 5 + 5 + 8 + 12,
        total_tut_ms = 3200.0 + 3300.0 + 3400.0 + 4500.0,
        avg_velocity_mps = 0.40,
        est_1rm_best_kg = 175.0,
        dots = 395.0,
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
    val exercise_snapshots_for_user2 = listOf(
        ExerciseMetricSnapshot(
            user_id = 2,
            exercise_id = 10,
            period_start = weekStart(),
            period_end = weekEnd(),
            total_volume_kg = 140.0 * 10,
            top_est_1rm_kg = 175.0,
            avg_tut_ms = 3250.0,
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

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalTime::class)
    val analytics_for_user2 = PersonalAnalytics(
        user_id = 2,
        measured_at = Instant.now(),
        weight_kg = 82.5,
        height_cm = 178.0,
        bodyfat_pct = 14.0,
        maintenance_calorie = 2900.0,
        age = 27,
        gender = Gender.MALE
    )
}
package com.example.exertion.data.ec

data class ECRepData(
    val repIndex: Int,
    val eccentricMs: Double,
    val concentricMs: Double,
    val tutMs: Double,
    val velocity: Double?,
    val romDeg: Double?
)

data class ECSetData(
    val workoutExerciseId: Int,
    val weightKg: Double?,
    val setIndex: Int,
    val reps: List<ECRepData>,
    val rir: Double? = null,
    val rpe: Double? = null
)
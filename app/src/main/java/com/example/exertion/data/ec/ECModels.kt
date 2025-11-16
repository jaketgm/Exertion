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

enum class ECPhase {
    IDLE,
    ECCENTRIC,
    CONCENTRIC
}

data class ECFrameMetrics(
    val timestampMs: Long,
    val romFraction: Float, // 0f..1f (0 = bottom, 1 = top) normalized from box center
    val velocity: Float, // change in romFraction / second (signed)
    val phase: ECPhase
)

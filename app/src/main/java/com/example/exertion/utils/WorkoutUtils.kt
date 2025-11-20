package com.example.exertion.utils

fun waveLabelForSet(setIndex: Int): String {
    // Very simple: sets 1–3 → Wave I, 4–6 → Wave II, 7–9 → Wave III
    return when ((setIndex - 1) / 3) {
        0 -> "Wave I"
        1 -> "Wave II"
        else -> "Wave III"
    }
}

fun estimateOneRm(weightKg: Double?, reps: Int?): Double? {
    if (weightKg == null || reps == null || reps <= 0) return null
    // Epley formula
    return weightKg * (1.0 + reps / 30.0)
}
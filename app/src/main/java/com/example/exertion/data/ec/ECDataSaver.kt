package com.example.exertion.data.ec

import com.example.exertion.data.set_entry.SetEntryVM
import com.example.exertion.data.rep_entry.RepEntryVM
import com.example.exertion.data.set_entry.SetEntry
import com.example.exertion.data.rep_entry.RepEntry

suspend fun saveECSetToDB(
    setVM: SetEntryVM,
    repVM: RepEntryVM,
    data: ECSetData
): Int {
    val timestamp = System.currentTimeMillis()

    // ---- Insert SET_ENTRY ----
    val setEntry = SetEntry(
        set_id = 0,
        workout_exercise_id = data.workoutExerciseId,
        set_index = data.setIndex,
        set_type = "standard",
        reps = data.reps.size,
        weight_kg = data.weightKg,
        tut_ms = data.reps.sumOf { it.tutMs },
        rir = data.rir,
        rpe = data.rpe,
        rest_sec = null,
        is_warmup = false,
        is_failure = false,
        timestamp = timestamp,
        top = 0.0,
        bottom = 0.0,
        tempo_notation = null,
        velocity_loss_pct = null,
        rir_suggested = null,
        rpe_suggested = null,
        suggestion_confidence = null
    )

    val setId = setVM.addSetEntryReturningId(setEntry)

    // ---- Insert REP_ENTRY rows ----
    data.reps.forEach { rep ->
        val repEntry = RepEntry(
            rep_id = 0,
            set_id = setId,
            rep_index = rep.repIndex,
            concentric_ms = rep.concentricMs,
            eccentric_ms = rep.eccentricMs,
            tut_ms = rep.tutMs,
            velocity_mps = rep.velocity,
            rom_deg = rep.romDeg,
            successful = true,
            timestamp = timestamp
        )
        repVM.addRepEntry(repEntry)
    }

    return setId
}
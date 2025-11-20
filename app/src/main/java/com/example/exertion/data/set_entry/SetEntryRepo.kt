package com.example.exertion.data.set_entry

import com.example.exertion.data.set_entry.read_dao.SetEntryReadDao
import com.example.exertion.data.set_entry.write_dao.SetEntryWriteDao
import kotlinx.coroutines.flow.Flow

class SetEntryRepo(
    private val setEntryReadDao: SetEntryReadDao,
    private val setEntryWriteDao: SetEntryWriteDao
) {
    fun readAllSetEntryData(): Flow<List<SetEntry>> = setEntryReadDao.readAllSetEntryData()

    suspend fun addSetEntryReturningId(setEntry: SetEntry): Long {
        return setEntryWriteDao.addSetEntry(setEntry)
    }

    suspend fun addSetEntry(setEntry: SetEntry) {
        setEntryWriteDao.addSetEntry(setEntry)
    }

    suspend fun deleteSet(id: Int) {
        setEntryWriteDao.deleteSetById(id)
    }

    suspend fun addSetForWorkoutExercise(workoutExerciseId: Int) {

        val nextIndex = setEntryWriteDao.getNextSetIndex(workoutExerciseId) + 1

        val newSet = SetEntry(
            set_id = 0,
            workout_exercise_id = workoutExerciseId,
            set_index = nextIndex,

            set_type = null,
            reps = 0,
            weight_kg = null,
            tut_ms = 0.0,
            rir = null,
            rpe = null,
            rest_sec = null,

            is_warmup = false,
            is_failure = false,

            timestamp = System.currentTimeMillis(),

            top = 0.0,
            bottom = 0.0,
            tempo_notation = null,
            velocity_loss_pct = null,

            rir_suggested = null,
            rpe_suggested = null,
            suggestion_confidence = null
        )

        setEntryWriteDao.addSetEntry(newSet)
    }
}
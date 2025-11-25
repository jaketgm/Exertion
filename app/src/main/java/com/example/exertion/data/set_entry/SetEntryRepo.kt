package com.example.exertion.data.set_entry

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.exertion.data.set_entry.read_dao.SetEntryReadDao
import com.example.exertion.data.set_entry.write_dao.SetEntryWriteDao
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import java.time.Instant

class SetEntryRepo(
    private val setEntryReadDao: SetEntryReadDao,
    private val setEntryWriteDao: SetEntryWriteDao
) {

    fun readAllSetEntryData(): Flow<List<SetEntry>> =
        setEntryReadDao.readAllSetEntryData()

    suspend fun addSetEntryReturningId(setEntry: SetEntry): Long {
        val ids = setEntryWriteDao.addSetEntries(listOf(setEntry))
        return ids.first()   // return the generated ID
    }

    suspend fun addSetEntry(setEntry: SetEntry) {
        setEntryWriteDao.addSetEntries(listOf(setEntry))
    }

    suspend fun deleteSet(id: Int) {
        setEntryWriteDao.deleteSetById(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalTime::class)
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

            timestamp = Instant.now(),
            top = 0.0,
            bottom = 0.0,
            tempo_notation = null,
            velocity_loss_pct = null,

            rir_suggested = null,
            rpe_suggested = null,
            suggestion_confidence = null
        )

        setEntryWriteDao.addSetEntries(listOf(newSet))
    }
}

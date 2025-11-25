package com.example.exertion.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.exertion.data.workout_exercise.WorkoutExercise
import com.example.exertion.data.set_entry.SetEntry
import com.example.exertion.data.workout.Workout

data class WorkoutExerciseWithSets(
    @Embedded val workoutExercise: WorkoutExercise,
    @Relation(
        parentColumn = "workout_exercise_id",
        entityColumn = "workout_exercise_id",
        entity = SetEntry::class
    )
    val sets: List<SetEntry>
)

data class WorkoutWithExercises(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "workout_id",
        entityColumn = "workout_id",
        entity = WorkoutExercise::class
    )
    val exercises: List<WorkoutExerciseWithSets>
)
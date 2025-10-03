package com.example.exertion.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*
import com.example.exertion.data.user_table.UserTable

@Entity(
    tableName = "workout",
    foreignKeys = [
        ForeignKey(
            entity = UserTable::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id", "started_at"], unique = true) // candidate key
    ]
)
data class WORKOUT(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    val workout_id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Int,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "started_at")
    val started_at: Long,

    @ColumnInfo(name = "ended_at")
    val ended_at: Long?, // >= started_at; null while in-progress

    @ColumnInfo(name = "kind", defaultValue = "session")
    val kind: String = "session",

    @ColumnInfo(name = "mesocycle_name")
    val mesocycle_name: String?,

    @ColumnInfo(name = "mesocycle_week")
    val mesocycle_week: Int?,

    @ColumnInfo(name = "notes")
    val notes: String?
)
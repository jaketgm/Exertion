package com.example.exertion.data.rep_entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*
import com.example.exertion.data.set_entry.SetEntry

@Entity(
    tableName = "rep_entry",
    foreignKeys = [
        // SET_ENTRY.set_id < REP_ENTRY.set_id [delete: cascade]
        ForeignKey(
            entity = SetEntry::class,
            parentColumns = ["set_id"],
            childColumns = ["set_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["set_id", "rep_index"], unique = true), // 1-based order within set, unique per (set, index)
        Index(value = ["set_id"])
    ]
)
data class RepEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rep_id")
    val rep_id: Int = 0,

    @ColumnInfo(name = "set_id")
    val set_id: Int, // Foreign Key -> SET_ENTRY

    @ColumnInfo(name = "rep_index")
    val rep_index: Int,

    @ColumnInfo(name = "concentric_ms")
    val concentric_ms: Double?, // >= 0

    @ColumnInfo(name = "eccentric_ms")
    val eccentric_ms: Double?, // >= 0

    @ColumnInfo(name = "tut_ms")
    val tut_ms: Double?, // >= 0

    @ColumnInfo(name = "velocity_mps")
    val velocity_mps: Double?, // >= 0

    @ColumnInfo(name = "rom_deg")
    val rom_deg: Double?, // >= 0

    @ColumnInfo(name = "successful", defaultValue = "1")
    val successful: Boolean = true,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
)
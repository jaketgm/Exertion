package com.example.exertion.data.user_table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_table",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["username"], unique = true)
    ]
)
data class UserTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val user_id: Int = 0,

    @ColumnInfo(name = "username", collate = ColumnInfo.NOCASE)
    val username: String?,

    @ColumnInfo(name = "email", collate = ColumnInfo.NOCASE)
    val email: String,

    @ColumnInfo(name = "password_hash")
    val password_hash: String?,

    @ColumnInfo(name = "created_at")
    val created_at: Long,

    // ------------------------
    // NEW SETTINGS FIELDS
    // ------------------------
    @ColumnInfo(name = "age")
    val age: Int? = null,

    @ColumnInfo(name = "weight_kg")
    val weight_kg: Double? = null,

    @ColumnInfo(name = "height_cm")
    val height_cm: Double? = null,

    @ColumnInfo(name = "gender")
    val gender: String? = null
)
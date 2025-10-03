package com.example.exertion.data.personal_analytics

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.exertion.data.Gender
import com.example.exertion.data.user_table.UserTable

@Entity(
    tableName = "personal_analytics",
    foreignKeys = [
        ForeignKey(
            entity = UserTable::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id", "measured_at"], unique = true)
    ]
)
data class PersonalAnalytics(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "entry_id")
    val entry_id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Int,

    @ColumnInfo(name = "measured_at")
    val measured_at: Long,

    @ColumnInfo(name = "weight_kg")
    val weight_kg: Double?, // nullable; however, if null => DOTS will be null, since it needs user bodyweight

    @ColumnInfo(name = "height_cm")
    val height_cm: Double?, // nullable

    @ColumnInfo(name = "bodyfat_pct")
    val bodyfat_pct: Double?, // need to restrict this to domain: [0..100] also nullable

    @ColumnInfo(name = "maintenance_calorie")
    val maintenance_calorie: Double?, // nullable

    @ColumnInfo(name = "age")
    val age: Int?, // nullable + need to restrict s.t. > 0

    @ColumnInfo(name = "gender")
    val gender: Gender?, // nullable + TODO: implement converter in DAO
)
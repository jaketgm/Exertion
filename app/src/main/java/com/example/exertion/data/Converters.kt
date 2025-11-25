package com.example.exertion.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant

enum class Gender {
    MALE, FEMALE, OTHER
}

object Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    @JvmStatic
    fun fromInstant(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    @JvmStatic
    fun toInstant(epochMillis: Long?): Instant? {
        return epochMillis?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    @JvmStatic
    fun fromGender(g: Gender?): String? {
        return g?.name
    }

    @TypeConverter
    @JvmStatic
    fun toGender(name: String?): Gender? {
        return name?.let { Gender.valueOf(it) }
    }
}
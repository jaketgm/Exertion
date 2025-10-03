package com.example.exertion.data

import androidx.room.TypeConverter

enum class Gender {
    MALE, FEMALE, OTHER
}

class Converters {
    @TypeConverter fun fromGender(value: Gender?): String? = value?.name
    @TypeConverter fun toGender(value: String?): Gender? = value?.let { Gender.valueOf(it) }
}
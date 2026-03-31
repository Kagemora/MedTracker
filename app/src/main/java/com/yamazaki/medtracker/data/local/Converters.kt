package com.yamazaki.medtracker.data.local

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromDaysList(days: List<Int>): String =
        days.joinToString(",")

    @TypeConverter
    fun toDaysList(days: String): List<Int> =
        if (days.isBlank()) emptyList()
        else (days.split(",").map { it.trim().toInt() })
}
package org.tty.dailyset.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime

class StringLocalDateTimeConverter {
    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }
}
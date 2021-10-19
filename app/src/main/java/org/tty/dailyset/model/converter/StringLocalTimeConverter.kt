package org.tty.dailyset.model.converter

import androidx.room.TypeConverter
import java.time.LocalTime

class StringLocalTimeConverter {

    @TypeConverter
    fun localTimeToString(value: LocalTime): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToLocalTime(value: String): LocalTime {
        return LocalTime.parse(value)
    }
}
package org.tty.dailyset.bean.converters

import androidx.room.TypeConverter
import java.time.LocalTime

class LocalTimeConverter {

    @TypeConverter
    fun localTimeToString(value: LocalTime): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToLocalTime(value: String): LocalTime {
        return LocalTime.parse(value)
    }
}
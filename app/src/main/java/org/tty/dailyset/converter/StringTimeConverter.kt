package org.tty.dailyset.converter

import androidx.room.TypeConverter
import java.sql.Time

class StringTimeConverter {
    @TypeConverter
    fun stringToTime(value: String): Time {
        return Time.valueOf(value)
    }

    @TypeConverter
    fun timeToString(time: Time): String {
        return time.toString()
    }
}
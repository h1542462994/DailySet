package org.tty.dailyset.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class StringLocalDateConverter {

    @TypeConverter
    fun localDateToString(localDate: LocalDate): String {
        return localDate.toString()
    }

    @TypeConverter
    fun stringToLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }
}
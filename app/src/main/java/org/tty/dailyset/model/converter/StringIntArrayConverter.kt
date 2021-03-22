package org.tty.dailyset.model.converter

import androidx.room.TypeConverter

class StringIntArrayConverter {
    @TypeConverter
    fun stringToIntArray(value: String): IntArray {
        return value.split(",").map { it.toInt() }.toIntArray()
    }

    @TypeConverter
    fun intArrayToString(value: IntArray): String {
        return value.joinToString(separator = ",")
    }
}
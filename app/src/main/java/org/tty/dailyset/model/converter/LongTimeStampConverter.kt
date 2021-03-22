package org.tty.dailyset.model.converter

import androidx.room.TypeConverter
import java.sql.Timestamp

class LongTimeStampConverter {
    @TypeConverter
    fun longToTimestamp(value: Long): Timestamp {
        return Timestamp(value)
    }

    @TypeConverter
    fun timestampToLong(timestamp: Timestamp): Long {
        return timestamp.time
    }
}
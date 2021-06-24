package org.tty.dailyset.model.converter

import androidx.room.TypeConverter
import org.tty.dailyset.model.entity.DailyDurationTag

class DailyDurationTagConverter {

    @TypeConverter
    fun dailyDurationTagToString(dailyDurationTag: DailyDurationTag): String {
        return dailyDurationTag.key
    }

    @TypeConverter
    fun stringToDailyDurationTag(key: String): DailyDurationTag {
        return DailyDurationTag.values().single { it.key == key }
    }
}
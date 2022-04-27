package org.tty.dailyset.converter

import androidx.room.TypeConverter
import org.tty.dailyset.bean.entity.DailyDurationTag

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
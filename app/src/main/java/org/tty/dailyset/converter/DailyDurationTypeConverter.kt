package org.tty.dailyset.converter

import androidx.room.TypeConverter
import org.tty.dailyset.bean.entity.DailyDurationType

class DailyDurationTypeConverter {

    @TypeConverter
    fun dailyDurationTypeToString(dailyDurationType: DailyDurationType): String {
        return dailyDurationType.key
    }

    @TypeConverter
    fun stringToDailyDuration(key: String): DailyDurationType {
        return DailyDurationType.values().single { it.key == key }
    }
}
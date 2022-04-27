package org.tty.dailyset.converter

import androidx.room.TypeConverter
import org.tty.dailyset.bean.entity.DailySetIcon

class DailySetIconConverter {
    @TypeConverter
    fun dailySetIconToString(dailySetIcon: DailySetIcon?): String? {
        return dailySetIcon?.key
    }

    @TypeConverter
    fun stringToDailySetIcon(key: String?): DailySetIcon? {
        return if (key == null) {
            null
        } else {
            DailySetIcon.values().single { it.key == key }
        }
    }
}
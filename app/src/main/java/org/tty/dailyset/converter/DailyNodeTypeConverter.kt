package org.tty.dailyset.converter

import androidx.room.TypeConverter
import org.tty.dailyset.bean.entity.DailyNodeType

class DailyNodeTypeConverter {
    @TypeConverter
    fun dailyNodeTypeToString(dailyNodeType: DailyNodeType): String {
        return dailyNodeType.key
    }

    @TypeConverter
    fun stringToDailyNodeType(key: String): DailyNodeType {
        return DailyNodeType.values().single { it.key == key }
    }
}
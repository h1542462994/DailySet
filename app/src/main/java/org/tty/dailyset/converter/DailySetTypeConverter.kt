package org.tty.dailyset.converter
import androidx.room.TypeConverter
import org.tty.dailyset.bean.entity.DailySetType

class DailySetTypeConverter {
    @TypeConverter
    fun dailySetTypeToString(dailySetType: DailySetType): String {
        return dailySetType.key
    }

    @TypeConverter
    fun stringToDailySetType(key: String): DailySetType {
        return DailySetType.values().single { it.key == key}
    }
}
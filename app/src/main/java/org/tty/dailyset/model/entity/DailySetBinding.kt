package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.model.converter.LongTimeStampConverter
import java.sql.Timestamp

/**
 * the dailySet and the dailyDuration is all the global data entry.
 * the binding is the relation between
 */
@Entity(tableName = "daily_set_binding", primaryKeys = ["dailySetUid", "dailyDurationUid"])

@TypeConverters(LongTimeStampConverter::class)
data class DailySetBinding(
    /**
     * the binding dailySetUid
     */
    val dailySetUid: String = "",
    /**
     * the binding dailyDurationUid
     * the binding DailyTable
     */

    val dailyDurationUid: String = "",
    /**
     * the property only used on [DailyDurationType.Clazz]
     */
    val bindingDailyTableUid: String = "",
    /**
     * the property only used on [DailyDurationType.Clazz]
     * usually 0/.. means unspecified, 1 means 上学期, 7 means 下学期, 13 means 短学期
     * 2 means 上学期期末 8 means 下学期期末
     * 4 means 寒假 14 means 暑假
     */
    val bindingPeriodCode: Int = 0,
    val updateAt: Timestamp,
)


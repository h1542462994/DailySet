package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.TypeConverters
import org.tty.dailyset.common.datetime.epochLocalDateTime
import org.tty.dailyset.model.converter.StringLocalDateTimeConverter
import java.time.LocalDateTime

/**
 * the dailySet and the dailyDuration is all the global data entry.
 * the binding is the relation between
 */
@Entity(tableName = "daily_set_binding", primaryKeys = ["dailySetUid", "dailyDurationUid"], indices = [
    Index("dailySetUid"), Index("dailyDurationUid"), Index("dailySetUid", "dailyDurationUid")
])

@TypeConverters(StringLocalDateTimeConverter::class)
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
    val updateAt: LocalDateTime,
) {
    companion object {
        fun empty(): DailySetBinding {
            return DailySetBinding(
                updateAt = epochLocalDateTime()
            )
        }
    }
}


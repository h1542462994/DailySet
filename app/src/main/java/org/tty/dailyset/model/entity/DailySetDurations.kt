package org.tty.dailyset.model.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.tty.dailyset.common.datetime.DateSpan
import java.time.DayOfWeek

data class DailySetDurations(
    @Embedded val dailySet: DailySet,
    @Relation(
        parentColumn = "uid",
        entityColumn = "uid",
        associateBy = Junction(DailySetBinding::class, parentColumn = "dailySetUid", entityColumn = "dailyDurationUid")
    )
    val durations: List<DailyDuration>
) {
    companion object {
        fun empty(): DailySetDurations {
            return DailySetDurations(
                dailySet = DailySet.empty(),
                durations = listOf()
            )
        }
    }
}

fun List<DailyDuration>.weekCountAtIndex(index: Int, startWeekDay: DayOfWeek): Int {
    val durations = this
    require(index in this.indices)
    val duration = durations[index]
    return DateSpan(duration.startDate, duration.endDate, startDayOfWeek = startWeekDay).weekCountFull
}

data class DurationDailySets(
    @Embedded val duration: DailyDuration,
    @Relation(
        parentColumn = "uid",
        entityColumn = "uid",
        associateBy = Junction(DailySetBinding::class, parentColumn = "dailyDurationUid", entityColumn = "dailySetUid")
    )
    val dailySets: List<DailySet>
)
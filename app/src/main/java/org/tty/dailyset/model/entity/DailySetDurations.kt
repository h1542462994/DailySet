package org.tty.dailyset.model.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.tty.dailyset.weekCount

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

fun List<DailyDuration>.weekCountAtIndex(index: Int): Int {
    val durations = this
    require(index in this.indices)
    val duration = durations[index]
    val end = if (index < durations.size - 1) {
        durations[index + 1].startDate
    } else {
        durations.last().endDate.plusDays(1)
    }
    val start = duration.startDate
    return weekCount(end, start).toInt()
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
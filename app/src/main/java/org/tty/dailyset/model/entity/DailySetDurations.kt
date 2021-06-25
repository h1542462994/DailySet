package org.tty.dailyset.model.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DailySetDurations(
    @Embedded val dailySet: DailySet,
    @Relation(
        parentColumn = "dailySetUid",
        entityColumn = "dailyDurationUid",
        associateBy = Junction(DailySetBinding::class)
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



data class DurationDailySets(
    @Embedded val duration: DailyDuration,
    @Relation(
        parentColumn = "dailyDurationUid",
        entityColumn = "dailySetUid",
        associateBy = Junction(DailySetBinding::class)
    )
    val dailySets: List<DailySet>
)
package org.tty.dailyset.model.lifetime.dailyset

/**
 * the cursor of the current showed dailySet
 */
data class ClazzDailySetCursor(
    val dailyDurationUid: String,
    val index: Int
) {
    companion object {
        fun empty(): ClazzDailySetCursor {
            return ClazzDailySetCursor(
                dailyDurationUid = "",
                index = 0
            )
        }
//        fun createFrom(dailySetDurations: DailySetDurations): ClazzDailySetCursor {
//            val date = LocalDate.now()
//            val durations = dailySetDurations.durations.sortedBy { it.startDate }
//            var cursor: ClazzDailySetCursor? = null
//            if (date.isBefore(durations.first().startDate)) {
//                cursor = ClazzDailySetCursor(
//                    dailyDurationUid = durations.first().uid,
//                    index = 0
//                )
//            } else if (date.isAfter(durations.last().endDate)) {
//                cursor = ClazzDailySetCursor(
//                    dailyDurationUid = durations.last().uid,
//                    index = weekCount(
//                        durations.last().endDate,
//                        durations.last().startDate
//                    ).toInt() - 1
//                )
//            } else {
//                for (index in durations.indices) {
//                    val duration = durations[index]
//                    val end = if (index < durations.size - 1) {
//                        durations[index + 1].startDate
//                    } else {
//                        durations.last().endDate.plusDays(1)
//                    }
//                    val start = duration.startDate
//
//                    if (date < end) {
//                        cursor = ClazzDailySetCursor(
//                            dailyDurationUid = duration.uid,
//                            index = weekCount(date, start).toInt() - 1
//                        )
//                        break
//                    }
//                }
//            }
//            return cursor!!
//        }

    }
}
package org.tty.dailyset.model.lifetime.dailyset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.tty.dailyset.common.datetime.DateSpan
import org.tty.dailyset.common.datetime.toWeekStart
import org.tty.dailyset.common.observable.state
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.model.entity.DailyDuration
import org.tty.dailyset.model.entity.DailySet
import org.tty.dailyset.model.lifetime.dailytable.DailyTablePreviewState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableState2
import org.tty.dailyset.viewmodel.MainViewModel
import org.tty.dioc.util.optional
import java.time.DayOfWeek

class ClazzDailySetState(
    private val cursors: ClazzDailySetCursors,
    val dailyTableState2: DailyTableState2,
    private val startDayOfWeek: DayOfWeek,
    private val mainViewModel: MainViewModel?
) {
    private val dailySetDurations = cursors.dailySetDurations
    val index = cursors.index
    val dailySet: DailySet get() = dailySetDurations.dailySet
    val durations: List<DailyDuration>
        get() = dailySetDurations.durations.sortedBy {
            it.startDate
        }
    val hasPrevCursor: Boolean = index > 0
    val hasNextCursor: Boolean = index < cursors.size - 1
    val currentDailyDuration: DailyDuration? get() {
        return dailySetDurations.durations.find {
            cursors[index].dailyDuration == it
        }
    }
    val cursor: ClazzDailySetCursor get() {
        return if (index in cursors.indices) {
            cursors[index]
        } else {
            ClazzDailySetCursor.empty()
        }
    }
    private val dateSpan: DateSpan
        get() {
            //val cursor = cursor
            //logger.d(Tags.liveDataExtension, "cursor = $cursor")
            val startDate =
                cursor.dailyDuration.startDate.toWeekStart(startDayOfWeek)
                    .plusDays((7 * cursor.index).toLong())
            val endDate = startDate.plusDays((7 - 1).toLong())
            //logger.d(Tags.liveDataExtension, "dateSpan = $dateSpan")
            return DateSpan(
                startDate = startDate,
                endDateInclusive = endDate,
                startDayOfWeek = startDayOfWeek
            )
        }

    @Composable
    fun previewState(): DailyTablePreviewState {
        with(DataScope) {
            val dateSpan = dateSpan
            val nowDate by nowDate()
            return DailyTablePreviewState(
                dateSpan,
                nowDate(),
                state(value = nowDate.dayOfWeek, key1 = nowDate),
                startWeekDay()
            )
        }
    }

    /**
     * change the current cursor to previous
     */
    fun toPrevCursor() {
        mainViewModel.optional {
            clazzCursorIndexFlow.postValue(index - 1)
        }
    }

    /**
     * change the current cursor to next
     */
    fun toNextCursor() {
        mainViewModel.optional {
            clazzCursorIndexFlow.postValue(index + 1)
        }
    }

    override fun toString(): String {
        return "ClazzDailySetState(page=[$index/${cursors.size - 1}], dailyTable=${dailyTableState2.dailyTRC.dailyTable.name})"
    }

    companion object {
        fun empty(): ClazzDailySetState {
            return ClazzDailySetState(
                ClazzDailySetCursors.empty(),
                DailyTableState2.default(),
                DayOfWeek.MONDAY,
                null
            )
        }
    }

}
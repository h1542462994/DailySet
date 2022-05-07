package org.tty.dailyset.bean.lifetime.dailyset

/*
import androidx.compose.runtime.Composable
import org.tty.dailyset.common.datetime.DateSpan
import org.tty.dailyset.common.datetime.toWeekStart
import org.tty.dailyset.database.scope.DataScope
import org.tty.dailyset.bean.entity.DailyDuration
import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.lifetime.dailytable.DailyTablePreviewState
import org.tty.dailyset.bean.lifetime.dailytable.DailyTableState2
import org.tty.dailyset.provider.mainViewModel
import org.tty.dailyset.component.common.MainViewModel
import org.tty.dioc.util.optional
import java.time.DayOfWeek

class ClazzDailySetState(
    private val cursors: ClazzDailySetCursors,
    */
/**
     * the current index of the [ClazzDailySetCursors]
     *//*

    val index: Int,
    val dailyTableState2: DailyTableState2,
    private val startDayOfWeek: DayOfWeek,
    private val mainViewModel: MainViewModel?
) {
    private val dailySetDurations = cursors.dailySetDurations
    val dailySet: DailySet get() = dailySetDurations.dailySet
    val durations: List<DailyDuration>
        get() = dailySetDurations.durations.sortedBy {
            it.startDate
        }

    */
/**
     * whether has previous page
     *//*

    val hasPrevCursor: Boolean = index > 0

    */
/**
     * whether has next page
     *//*

    val hasNextCursor: Boolean = index < cursors.size - 1

    */
/**
     * the current dailyDuration
     *//*

    val currentDailyDuration: DailyDuration? get() {
        return dailySetDurations.durations.find {
            cursors[index].dailyDuration == it
        }
    }

    */
/**
     * the current cursor
     *//*

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
            return DailyTablePreviewState(
                dateSpan,
                nowDate(),
                clazzWeekDay(),
                startWeekDay()
            )
        }
    }

    */
/**
     * change the current cursor to previous
     *//*

    fun toPrevCursor() {
        mainViewModel.optional {
            if (hasPrevCursor) clazzCursorIndexLiveData.postValue(index - 1)
        }
    }

    */
/**
     * change the current cursor to next
     *//*

    fun toNextCursor() {
        mainViewModel.optional {
            if (hasNextCursor) clazzCursorIndexLiveData.postValue(index + 1)
        }
    }



    override fun toString(): String {
        return "ClazzDailySetState(page=[$index/${cursors.size - 1}], dailyTable=${dailyTableState2.dailyTRC.dailyTable.name})"
    }

    companion object {
        fun empty(): ClazzDailySetState {
            return ClazzDailySetState(
                ClazzDailySetCursors.empty(),
                0,
                DailyTableState2.default(),
                DayOfWeek.MONDAY,
                null
            )
        }

        */
/**
         * change the current cursor to indexed page.
         *//*

        fun toIndexedPage(index: Int) {
            mainViewModel.clazzCursorIndexLiveData.postValue(index)
        }
    }

}*/

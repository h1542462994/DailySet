package org.tty.dailyset.model.lifetime.dailytable

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import java.time.LocalDate

/**
 * A complex state for DailyTablePreviewPage
 */
class DailyTablePreviewState(
    /**
     * currentDate, means now.
     */
    val startDate: LocalDate,
    /**
     * the weekday of now, if null, mean today is not in current week.
     */
    val weekDayNow: Int?,
    private val _weedDayCurrent: MutableState<Int>
){
    /**
     * the selected weekDay, it's handled by state.
     */
    val weekDayCurrent by _weedDayCurrent
    val setWeekDayCurrent: (Int) -> Unit = { value ->
        _weedDayCurrent.value = value
    }
}
package org.tty.dailyset.bean.lifetime.dailytable

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import org.tty.dailyset.common.datetime.DateSpan
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * A complex state for DailyTablePreviewPage
 */
class DailyTablePreviewState(
    /**
     * currentDate, means now.
     */
    val dateSpan: DateSpan,
    /**
     * nowDate
     */
    val nowDate: State<LocalDate>,
    val currentWeekDay: MutableState<DayOfWeek>,
    val startWeekDay: State<DayOfWeek>
){

}
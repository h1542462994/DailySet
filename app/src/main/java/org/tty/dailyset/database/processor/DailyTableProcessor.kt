package org.tty.dailyset.database.processor

import androidx.compose.runtime.Immutable
import org.tty.dailyset.bean.lifetime.WeekDayState
import java.time.LocalTime

@Deprecated("instead with component VM.")
/**
 * dailyTable related operations, used in ui interactive.
 */
@Immutable
interface DailyTableProcessor {
    fun onCreate(dailyTableName: String)
    fun onDelete()
    fun onAddRow(weekDays: List<WeekDayState>)
    fun onClickWeekDay(rowIndex: Int, weekDay: Int)
    fun onRename(name: String)
    fun onDeleteDailyRow(rowIndex: Int)
    fun onModifySection(rowIndex: Int, counts: IntArray)
    fun onModifyCell(rowIndex: Int, cellIndex: Int, start: LocalTime, end: LocalTime)
}
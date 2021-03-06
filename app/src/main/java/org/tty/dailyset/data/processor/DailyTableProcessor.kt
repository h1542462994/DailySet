package org.tty.dailyset.data.processor

import androidx.compose.runtime.Immutable
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.WeekDayState
import java.sql.Time

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
    fun onModifyCell(rowIndex: Int, cellIndex: Int, start: Time, end: Time)
}
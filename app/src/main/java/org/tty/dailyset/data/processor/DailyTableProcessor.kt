package org.tty.dailyset.data.processor

import androidx.compose.runtime.Immutable
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.WeekDayState

@Immutable
interface DailyTableProcessor {
    fun onCreate(dailyTableName: String, currentDailyTable: DailyTable)
    fun onDelete(dailyTRC: DailyTRC)
    fun onAddRow(weekDays: List<WeekDayState>)
}
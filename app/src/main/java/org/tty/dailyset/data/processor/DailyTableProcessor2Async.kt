package org.tty.dailyset.data.processor

import androidx.room.Ignore
import org.tty.dailyset.event.*

/**
 * DailyTable related operation used in dailyTableDao
 * @see [org.tty.dailyset.model.dao.DailyTableDao]
 */
interface DailyTableProcessor2Async: EventProcessorAsync {
    @Ignore
    override suspend fun performProcess(eventType: EventType, eventArgs: EventArgs) {
        when(eventType) {
            DailyTableEventType.Create -> { createFromTemplate(eventArgs as DailyTableCreateEventArgs) }
            DailyTableEventType.Delete -> { delete(eventArgs as DailyTableDeleteEventArgs) }
            DailyTableEventType.AddRow -> { addRow(eventArgs as DailyTableAddRowEventArgs) }
            DailyTableEventType.ClickWeekDay -> { clickWeekDay(eventArgs as DailyTableClickWeekDayEventArgs) }
            DailyTableEventType.Rename -> { rename(eventArgs as DailyTableRenameEventArgs) }
            else -> { TODO("not yet implemented") }
        }
    }
    suspend fun createFromTemplate(dailyTableCreateEventArgs: DailyTableCreateEventArgs)
    suspend fun delete(dailyTableDeleteEventArgs: DailyTableDeleteEventArgs)
    suspend fun addRow(dailyTableAddRowEventArgs: DailyTableAddRowEventArgs)
    suspend fun clickWeekDay(dailyTableClickWeekDayEventArgs: DailyTableClickWeekDayEventArgs)
    suspend fun rename(dailyTableRenameEventArgs: DailyTableRenameEventArgs)
}
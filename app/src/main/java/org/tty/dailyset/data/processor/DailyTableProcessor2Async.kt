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
            DailyTableEventType.create -> { createFromTemplate(eventArgs as DailyTableCreateEventArgs) }
            DailyTableEventType.delete -> { delete(eventArgs as DailyTableDeleteEventArgs) }
            DailyTableEventType.addRow -> { addRow(eventArgs as DailyTableAddRowEventArgs) }
            DailyTableEventType.clickWeekDay -> { clickWeekDay(eventArgs as DailyTableClickWeekDayEventArgs) }
            else -> { TODO("not yet implemented") }
        }
    }
    suspend fun createFromTemplate(dailyTableCreateEventArgs: DailyTableCreateEventArgs)
    suspend fun delete(dailyTableDeleteEventArgs: DailyTableDeleteEventArgs)
    suspend fun addRow(dailyTableAddRowEventArgs: DailyTableAddRowEventArgs)
    suspend fun clickWeekDay(dailyTableClickWeekDayEventArgs: DailyTableClickWeekDayEventArgs)
}
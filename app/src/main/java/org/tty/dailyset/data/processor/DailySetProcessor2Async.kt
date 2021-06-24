package org.tty.dailyset.data.processor

import org.tty.dailyset.event.DailySetCreateEventArgs
import org.tty.dailyset.event.DailySetEventType
import org.tty.dailyset.event.EventArgs
import org.tty.dailyset.event.EventType
import java.lang.IllegalArgumentException

/**
 * DailySet related operation used in dailySetDao
 * @see [org.tty.dailyset.model.dao.DailySetDao]
 */
interface DailySetProcessor2Async: EventProcessorAsync {
    override suspend fun performProcess(eventType: EventType, eventArgs: EventArgs) {
        when (eventType) {
            DailySetEventType.Create -> {
                create(eventArgs as DailySetCreateEventArgs)
            }
            else -> {
                throw IllegalArgumentException("eventType not supported.")
            }
        }
    }

    suspend fun create(dailySetCreateEventArgs: DailySetCreateEventArgs)
}
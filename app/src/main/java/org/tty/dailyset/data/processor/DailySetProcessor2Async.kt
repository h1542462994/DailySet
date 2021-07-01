package org.tty.dailyset.data.processor

import org.tty.dailyset.event.*
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
            DailySetEventType.CreateDurationAndBinding -> {
                createDuration(eventArgs as DailySetCreateDurationAndBindingEventArgs)
            }
            DailySetEventType.BindingDuration -> {
                bindingDuration(eventArgs as DailySetBindingDurationEventArgs)
            }
            else -> {
                throw IllegalArgumentException("eventType not supported.")
            }
        }
    }

    suspend fun create(dailySetCreateEventArgs: DailySetCreateEventArgs)
    suspend fun createDuration(dailySetCreateDurationAndBindingEventArgs: DailySetCreateDurationAndBindingEventArgs)
    suspend fun bindingDuration(dailySetBindingDurationEventArgs: DailySetBindingDurationEventArgs)
}
package org.tty.dailyset.database.processor

import org.tty.dailyset.event.EventArgs
import org.tty.dailyset.event.EventType

/**
 * a interface to process suspend function
 */
interface EventProcessorAsync {
    suspend fun performProcess(eventType: EventType, eventArgs: EventArgs)
}
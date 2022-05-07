package org.tty.dailyset.database.processor

import android.util.Log
import org.tty.dailyset.event.EventArgs
import org.tty.dailyset.event.EventType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Deprecated("instead with component VM.")
/**
 * callback version for [EventProcessorAsync]
 */
interface EventProcessorCallBack<T> {
    fun eventProcessorAsync(eventType: EventType, eventArgs: EventArgs, service: T): EventProcessorAsync

    fun performProcess(
        service: T,
        eventType: EventType,
        eventArgs: EventArgs,
        onBefore: (EventArgs) -> Unit,
        onCompletion: (EventArgs) -> Unit
    ) {
        Log.d("ServiceProcessor", "eventType=${eventType},eventArgs=${eventArgs}")
        onBefore(eventArgs)
        val job = GlobalScope.launch {
            eventProcessorAsync(eventType, eventArgs, service).performProcess(eventType, eventArgs)
        }
        job.invokeOnCompletion {
            onCompletion(eventArgs)
        }
    }
}
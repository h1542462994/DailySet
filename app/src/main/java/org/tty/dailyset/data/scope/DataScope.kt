package org.tty.dailyset.data.scope

import androidx.compose.runtime.Immutable
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.data.processor.EventProcessorAsync
import org.tty.dailyset.data.processor.EventProcessorCallBack
import org.tty.dailyset.event.DailyTableEventType
import org.tty.dailyset.event.EventArgs
import org.tty.dailyset.event.EventType

/**
 * root of data operations,
 * see also [DailyTableScope], [UserScope]
 * usage: with(DataScope) { ... }
 */
@Immutable
object DataScope: DailyTableScope, UserScope, DailySetScope, EventProcessorCallBack<DailySetApplication> {

    override fun eventProcessorAsync(
        eventType: EventType,
        eventArgs: EventArgs,
        service: DailySetApplication
    ): EventProcessorAsync {
        if (eventType is DailyTableEventType) {
            return service.dailyTableRepository
        } else {
            throw IllegalArgumentException("not found available processor")
        }
    }
}

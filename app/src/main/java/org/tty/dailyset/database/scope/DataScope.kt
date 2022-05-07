package org.tty.dailyset.database.scope

import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.database.processor.EventProcessorAsync
import org.tty.dailyset.database.processor.EventProcessorCallBack
import org.tty.dailyset.event.DailySetEventType
import org.tty.dailyset.event.DailyTableEventType
import org.tty.dailyset.event.EventArgs
import org.tty.dailyset.event.EventType

@Deprecated("instead with component VM.")
/**
 * root of data operations,
 * see also [DailyTableScope], [UserScope]
 * usage: with(DataScope) { ... }
 */
object DataScope: DailyTableScope, UserScope, DailySetScope, EventProcessorCallBack<SharedComponents> {

    override fun eventProcessorAsync(
        eventType: EventType,
        eventArgs: EventArgs,
        service: SharedComponents
    ): EventProcessorAsync {
        return when (eventType) {
            is DailyTableEventType -> {
                service.repositoryCollection.dailyTableRepository
            }
            is DailySetEventType -> {
                service.repositoryCollection.dailySetRepository
            }
            else -> {
                throw IllegalArgumentException("not found available processor")
            }
        }
    }
}

package org.tty.dailyset.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.tty.dailyset.common.observable.flow3
import org.tty.dailyset.model.entity.DailySetBinding
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetCursors
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableState2
import java.time.DayOfWeek

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(ExperimentalCoroutinesApi::class)
class ClazzDailySetStatePart(



    private val mainViewModel: MainViewModel,
    private val clazzDailySetCursorsFlow: Flow<ClazzDailySetCursors>,
    private val userUidFlow: Flow<String>,
    private val startWeekDayFlow: Flow<DayOfWeek>
) {
    private val cache = HashMap<Int, Flow<ClazzDailySetState>>()

    operator fun get(index: Int): Flow<ClazzDailySetState> {
        val item = cache[index]
        if (item != null) {
            return item
        }

        val clazzDailySetBindingFlow = clazzDailySetCursorsFlow
            .transform<ClazzDailySetCursors, DailySetBinding?> tran@ { cursors ->
                if (index in cursors.indices) {
                    val cursor = cursors[index]
                    emitAll(mainViewModel.service.dailySetRepository.loadDailySetBinding(
                        cursors.dailySetDurations.dailySet.uid,
                        cursor.dailyDuration.uid
                    ))
                } else {
                    emit(null)
                }
            }

        val clazzDailyTRCFlow = clazzDailySetBindingFlow
            .transform { value ->
                if (value != null) {
                    emitAll(mainViewModel.service.dailyTableRepository.loadDailyTRC(value.bindingDailyTableUid))
                } else {
                    emit(DailyTRC.default())
                }
            }
            .map { it ?: DailyTRC.default() }
        val clazzDailyTableState2Flow = clazzDailyTRCFlow.combine(userUidFlow) { value1, value2 ->
            DailyTableState2(value1, value2)
        }
        val clazzDailySetStateFlow = flow3(clazzDailySetCursorsFlow, clazzDailyTableState2Flow, startWeekDayFlow) { cursors, dailyTableState2, startWeekDay ->
            ClazzDailySetState(cursors, dailyTableState2, startWeekDay, mainViewModel)
        }

        cache[index] = clazzDailySetStateFlow
        return clazzDailySetStateFlow
    }

    val current: Flow<ClazzDailySetState> = clazzDailySetCursorsFlow.transform { value ->
        emitAll(get(value.index))
    }
}
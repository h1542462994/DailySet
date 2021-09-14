package org.tty.dailyset.model.lifetime.dailyset

import androidx.compose.runtime.mutableStateOf
import org.tty.dailyset.model.entity.DailyDuration
import org.tty.dailyset.model.entity.DailySet
import org.tty.dailyset.model.lifetime.dailytable.DailyTablePreviewState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableState2
import org.tty.dailyset.viewmodel.MainViewModel
import org.tty.dioc.util.optional
import java.time.LocalDate

class ClazzDailySetState(
    private val cursors: ClazzDailySetCursors,
    val dailyTableState2: DailyTableState2,
    val index: Int,
    private val mainViewModel: MainViewModel?
) {
    val dailySetDurations = cursors.dailySetDurations
    val dailySet: DailySet get() = dailySetDurations.dailySet
    val durations: List<DailyDuration>
        get() = dailySetDurations.durations.sortedBy {
            it.startDate
        }
    val hasPrevCursor: Boolean = index > 0
    val hasNextCursor: Boolean = index < cursors.list.size - 1
    val currentDailyDuration: DailyDuration? get() {
        return dailySetDurations.durations.find {
            cursors.list[index].dailyDurationUid == it.uid
        }
    }
    val cursor: ClazzDailySetCursor get() {
        return if (index in cursors.list.indices) {
            cursors.list[index]
        } else {
            ClazzDailySetCursor.empty()
        }
    }
    val duration: DailyDuration get() {
        return durations.find {
            it.uid == cursor.dailyDurationUid
        } ?: DailyDuration.empty()
    }

    val previewState: DailyTablePreviewState get() {
        return DailyTablePreviewState(
            LocalDate.now(),
            5,
            mutableStateOf(5)
        )
    }

    /**
     * change the current cursor to previous
     */
    fun toPrevCursor() {
        mainViewModel.optional {
            currentCursorIndexLiveData.postValue(index - 1)
        }
    }

    /**
     * change the current cursor to next
     */
    fun toNextCursor() {
        mainViewModel.optional {
            currentCursorIndexLiveData.postValue(index + 1)
        }
    }

    companion object {
        fun empty(): ClazzDailySetState {
            return ClazzDailySetState(
                ClazzDailySetCursors.empty(),
                DailyTableState2.default(),
                0,
                null
            )
        }
    }

}
package org.tty.dailyset.model.lifetime.dailyset

import androidx.compose.runtime.mutableStateOf
import org.tty.dailyset.model.entity.DailyDuration
import org.tty.dailyset.model.entity.DailySet
import org.tty.dailyset.model.entity.DailySetBinding
import org.tty.dailyset.model.entity.DailySetDurations
import org.tty.dailyset.model.lifetime.dailytable.DailyTablePreviewState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableState2
import org.tty.dailyset.toWeekStart
import org.tty.dailyset.viewmodel.MainViewModel
import org.tty.dailyset.weekCount
import java.time.LocalDate

class ClazzDailySetState(
    private val dailySetDurations: DailySetDurations,
    private val clazzDailySetCursorCache: HashMap<String, ClazzDailySetCursor>,
    val dailySetBinding: DailySetBinding,
    val dailyTableState2: DailyTableState2,
    val mainViewModel: MainViewModel?
) {
    val dailySet: DailySet get() = dailySetDurations.dailySet
    val durations: List<DailyDuration>
        get() = dailySetDurations.durations.sortedBy {
            it.startDate
        }


    val cursor: ClazzDailySetCursor?
        get() {
            return clazzDailySetCursorCache[dailySet.uid] ?: initCursor()
        }

    val hasPrevCursor: Boolean
        get() {
            val c = cursor
            return if (c != null) {
                !(c.dailyDurationUid == durations.first().uid && c.index == 0)
            } else {
                false
            }
        }

    val hasNextCursor: Boolean
        get() {
            val c = cursor
            return if (c != null) {
                val weekCount =
                    weekCount(durations.last().endDate, durations.last().startDate).toInt()
                !(c.dailyDurationUid == durations.last().uid && c.index == weekCount - 1)
            } else {
                false
            }
        }

    val currentDailyDuration: DailyDuration?
        get() {
            val c = cursor
            return if (c == null) {
                null
            } else {
                dailySetDurations.durations.find { it.uid == c.dailyDurationUid }
            }
        }

    private fun initCursor(): ClazzDailySetCursor? {
        // TODO: 2021/7/1 确定今天是星期几
        val date = LocalDate.now()
        var cursor: ClazzDailySetCursor? = null
        if (durations.isEmpty()) {
            return null
        } else {
            if (date.isBefore(durations.first().startDate)) {
                cursor = ClazzDailySetCursor(
                    dailyDurationUid = durations.first().uid,
                    index = 0
                )
            } else if (date.isAfter(durations.last().endDate)) {
                cursor = ClazzDailySetCursor(
                    dailyDurationUid = durations.last().uid,
                    index = weekCount(
                        durations.last().endDate,
                        durations.last().startDate
                    ).toInt() - 1
                )
            } else {
                for (index in durations.indices) {
                    val duration = durations[index]
                    val end = if (index < durations.size - 1) {
                        durations[index + 1].startDate
                    } else {
                        durations.last().endDate.plusDays(1)
                    }
                    val start = duration.startDate

                    if (date < end) {
                        cursor = ClazzDailySetCursor(
                            dailyDurationUid = duration.uid,
                            index = weekCount(date, start).toInt() - 1
                        )
                        break
                    }
                }
            }
            if (cursor != null) {
                clazzDailySetCursorCache[dailySet.uid] = cursor
                // update the current dailySetBinding
                mainViewModel!!.currentDailySetBindingKey.postValue(
                    Pair(
                        dailySet.uid,
                        cursor.dailyDurationUid
                    )
                )
            }
            return cursor!!
        }


    }

    val previewState: DailyTablePreviewState
        get() {
            val duration = currentDailyDuration
            val c = cursor
            if (duration != null && c != null) {
                val start = duration.startDate.toWeekStart()
                val currentStart = start.plusDays((7 * c.index).toLong())
                val currentWeekDayOfWeek = LocalDate.now().dayOfWeek.value
                val isCurrentWeek = currentStart.isEqual(LocalDate.now().toWeekStart())

                return DailyTablePreviewState(
                    currentStart,
                    if (isCurrentWeek) currentWeekDayOfWeek else null,
                    mutableStateOf(currentWeekDayOfWeek)
                )
            } else {
                throw IllegalStateException("currentDailyDuration null or cursor null.")
            }

        }


    override fun toString(): String {
        return "${dailySet.uid},${cursor}"
    }

    /**
     * change the current cursor to previous
     */
    fun toPrevCursor() {
        val c = cursor
        if (c != null) {
            if (c.index > 0) {
                clazzDailySetCursorCache[dailySet.uid] = ClazzDailySetCursor(
                    dailyDurationUid = c.dailyDurationUid,
                    index = c.index - 1
                )
                mainViewModel!!.currentDailySetBindingKey.postValue(
                    Pair(
                        dailySet.uid,
                        c.dailyDurationUid
                    )
                )
            } else {
                for (i in durations.indices) {
                    if (durations[i].uid == c.dailyDurationUid) {
                        if (i > 0) {
                            val weekCount =
                                weekCount(durations[i - 1].endDate, durations[i - 1].startDate).toInt()
                            clazzDailySetCursorCache[dailySet.uid] = ClazzDailySetCursor(
                                dailyDurationUid = durations[i - 1].uid,
                                index = weekCount - 1
                            )
                            mainViewModel!!.currentDailySetBindingKey.postValue(
                                Pair(
                                    dailySet.uid,
                                    durations[i - 1].uid
                                )
                            )
                            break
                        }
                    }
                }
            }
        }
    }

    /**
     * change the current cursor to next
     */
    fun toNextCursor() {
        val c = cursor
        if (c != null) {
            for (i in durations.indices) {
                if (durations[i].uid == c.dailyDurationUid) {
                    val cWeekCount =
                        weekCount(durations[i].endDate, durations[i].startDate).toInt()
                    if (c.index < cWeekCount - 1) {
                        clazzDailySetCursorCache[dailySet.uid] = ClazzDailySetCursor(
                            dailyDurationUid = c.dailyDurationUid,
                            index = c.index + 1
                        )
                        mainViewModel!!.currentDailySetBindingKey.postValue(
                            Pair(
                                dailySet.uid,
                                c.dailyDurationUid
                            )
                        )
                    } else {
                        if (i < durations.size - 1) {
                            clazzDailySetCursorCache[dailySet.uid] = ClazzDailySetCursor(
                                dailyDurationUid = durations[i + 1].uid,
                                index = 0
                            )
                            mainViewModel!!.currentDailySetBindingKey.postValue(
                                Pair(
                                    dailySet.uid,
                                    durations[i + 1].uid
                                )
                            )
                        }

                        break
                    }
                }
            }
        }
    }

    companion object {
        fun empty(): ClazzDailySetState {
            return ClazzDailySetState(
                DailySetDurations.empty(),
                HashMap(),
                DailySetBinding.empty(),
                DailyTableState2.default(),
                null
            )
        }
    }
}
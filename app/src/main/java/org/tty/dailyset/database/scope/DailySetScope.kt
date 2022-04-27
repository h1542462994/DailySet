package org.tty.dailyset.database.scope

import androidx.compose.runtime.*
import org.tty.dailyset.common.datetime.toWeekStart
import org.tty.dailyset.common.observable.state
import org.tty.dailyset.bean.entity.*
import org.tty.dailyset.bean.lifetime.PagerInfo
import org.tty.dailyset.bean.lifetime.dailyset.ClazzDailyDurationCreateState
import org.tty.dailyset.bean.lifetime.dailyset.ClazzDailySetState
import org.tty.dailyset.bean.lifetime.dailyset.DailySetCreateState
import java.time.DayOfWeek
import java.time.LocalDate
import org.tty.dailyset.provider.mainViewModel as vm

interface DailySetScope: PreferenceScope, UserScope, DailyTableScope {
    /**
     * the data entry for all dailySet.
     */
    @Composable
    fun dailySets(): State<List<DailySet>> {
        return state(vm.dailySets)
    }

    /**
     * the data entry for current dailySet and durations.
     */
    @Composable
    fun currentDailySetDurations(): State<DailySetDurations> {
        return state(vm.dailySetDurations)
    }

    @Composable
    fun currentDailySetUid(): MutableState<String> {
        return state(vm.dailySetUid)
    }

    @Composable
    @Deprecated("not used.")
    fun currentClazzDailySetState(): State<ClazzDailySetState> {
        return state(vm.clazzDailySetState)
    }

    @Composable
    fun clazzDailySetPagerInfo(): State<PagerInfo> {
        return state(vm.clazzDailySetPagerInfo)
    }

    @Composable
    fun clazzDailySetStateOfIndex(index: Int): State<ClazzDailySetState> {
        return state(vm.clazzDailySetStateOfIndex(index))
    }

    /**
     * the data entry for not included durations.
     */
    @Composable
    fun currentNotIncludedDurations(type: DailyDurationType): State<List<DailyDuration>> {
        val dailySetDurations by currentDailySetDurations()
        val durations = dailySetDurations.durations
        return if (type == DailyDurationType.Clazz) {
            val clazzDurations by clazzDailyDurations()
            derivedStateOf {
                clazzDurations.subtract(durations).toList()
            }
        } else {
            val normalDurations by normalDailyDurations()
            derivedStateOf {
                normalDurations.subtract(durations).toList()
            }
        }
    }

    /**
     * the data entry for normal dailyDurations
     */
    @Composable
    fun normalDailyDurations(): State<List<DailyDuration>> {
        return state(vm.normalDailyDurations)
    }

    /**
     * the data entry for clazz dailyDurations
     */
    @Composable
    fun clazzDailyDurations(): State<List<DailyDuration>> {
        return state(vm.clazzDailyDurations)
    }

    @Composable
    fun clazzWeekDay(): MutableState<DayOfWeek> {
        return state(vm.clazzWeekDay)
    }

    /**
     * create a state represents dailySetCreate dialog.
     */
    @Composable
    fun dailySetCreateState(
        initDialogOpen: Boolean = false,
    ): DailySetCreateState {
        return DailySetCreateState(
            dialogOpen = remember {
                mutableStateOf(initDialogOpen)
            },
            selectIcon = remember {
                mutableStateOf(false)
            },
            icon = remember {
                mutableStateOf(null)
            },
            type = remember {
                mutableStateOf(DailySetType.Normal)
            },
            name = remember {
                mutableStateOf("")
            }
        )
    }

    /**
     * create a state represents clazzDailyDurationCreate dialog
     */
    @Composable
    fun clazzDailyDurationCreateState(
        initDialogOpen: Boolean = false
    ): ClazzDailyDurationCreateState {
        val startWeekDay by startWeekDay()

        val startDate = LocalDate.now().toWeekStart(startWeekDay)
        val endDate = startDate.plusDays((7 * 16 - 1).toLong())
        val weekCount = 16

        return ClazzDailyDurationCreateState(
            dialogOpen = remember {
                mutableStateOf(initDialogOpen)
            },
            sourceUid = remember {
                mutableStateOf(null)
            },
            startDate = remember {
                mutableStateOf(startDate)
            },
            endDate = remember {
                mutableStateOf(endDate)
            },
            weekCount = remember {
                mutableStateOf(weekCount)
            },
            name = remember {
                mutableStateOf("")
            },
            periodCode = remember {
                mutableStateOf(PeriodCode.UnSpecified)
            },
            bindingDailyTableUid = remember {
                mutableStateOf(DailyTable.default)
            }
        )
    }

    companion object {
        const val TAG = "DailySetScope"
    }
}
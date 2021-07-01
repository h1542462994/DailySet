package org.tty.dailyset.data.scope

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import org.tty.dailyset.model.entity.*
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailyDurationCreateState
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetState
import org.tty.dailyset.model.lifetime.dailyset.DailySetCreateState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableState2
import org.tty.dailyset.toWeekStart
import java.time.LocalDate
import java.util.function.Predicate

interface DailySetScope: PreferenceScope, UserScope {
    /**
     * the data entry for all dailySet.
     */
    @Composable
    fun dailySets(): State<List<DailySet>> {
        val mainViewModel = mainViewModel()
        return mainViewModel.dailySets.observeAsState(initial = listOf())
    }

    /**
     * the data entry for current dailySet.
     */
    @Composable
    @Deprecated("use currentDailySetDurations instead.", level = DeprecationLevel.ERROR)
    fun currentDailySet(): State<DailySet> {
        val mainViewModel = mainViewModel()
        return mainViewModel.currentDailySet.observeAsState(initial = DailySet.empty())
    }

    /**
     * the data entry for current dailySet and durations.
     */
    @Composable
    fun currentDailySetDurations(): State<DailySetDurations> {
        val mainViewModel = mainViewModel()
        return mainViewModel.currentDailySetDurations.observeAsState(initial = DailySetDurations.empty())
    }

    @Composable
    fun currentClazzDailySetState(): State<ClazzDailySetState> {
        val currentDailySetDurations by currentDailySetDurations()
        val mainViewModel = mainViewModel()
        val dailySetCursorCache = mainViewModel.clazzDailySetCursorCache
        val dailySetBinding by mainViewModel.currentDailySetBinding.observeAsState(DailySetBinding.empty())
        val dailyTableState2 by mainViewModel.currentDailyTableState2Binding.observeAsState(
            DailyTableState2.default())
        return remember(key1 = listOf(currentDailySetDurations, dailySetBinding)) {
            mutableStateOf(ClazzDailySetState(currentDailySetDurations, dailySetCursorCache, dailySetBinding, dailyTableState2, mainViewModel))
        }
    }

    /**
     * the data entry for not included durations.
     */
    @Composable
    fun currentNotIncludedDurations(type: DailyDurationType): State<List<DailyDuration>> {
        val mainViewModel = mainViewModel()
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
        val mainViewModel = mainViewModel()
        return mainViewModel.normalDailyDurations.observeAsState(initial = listOf())
    }

    /**
     * the data entry for clazz dailyDurations
     */
    @Composable
    fun clazzDailyDurations(): State<List<DailyDuration>> {
        val mainViewModel = mainViewModel()
        return mainViewModel.clazzDailyDurations.observeAsState(initial = listOf())
    }

    /**
     * the empty dailyDurations, just for test
     */
    @Composable
    fun emptyDailyDurations(): State<List<DailyDuration>> {
        return remember {
            mutableStateOf(listOf())
        }
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
        val startDate = LocalDate.now().toWeekStart()
        val endDate = startDate.plusDays(7 * 16 - 1)
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
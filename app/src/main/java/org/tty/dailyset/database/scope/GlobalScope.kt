package org.tty.dailyset.database.scope

import androidx.compose.runtime.*
import org.tty.dailyset.component.common.DailySetApplication
import org.tty.dailyset.common.observable.state
import org.tty.dailyset.bean.lifetime.DatePickerDialogState
import org.tty.dailyset.provider.LocalMainViewModel
import org.tty.dailyset.ui.page.MainPageTabs
import org.tty.dailyset.component.common.MainViewModel
import org.tty.dailyset.component.common.SharedComponents
import java.time.DayOfWeek
import java.time.LocalDate
import org.tty.dailyset.provider.mainViewModel as vm

@Immutable
interface GlobalScope  {
    @Composable
    fun mainViewModel(): MainViewModel {
        return LocalMainViewModel.current
    }

    @Composable
    fun mainTab(): MutableState<MainPageTabs> {
        return state(vm.mainTab)
    }

    @Composable
    fun rootService(): SharedComponents {
        return vm.sharedComponents
    }

    @Composable
    fun nowDate(): State<LocalDate> {
        return state(vm.nowDate)
    }

    @Composable
    fun startWeekDay(): State<DayOfWeek> {
        return state(vm.startWeekDay)
    }

    @Composable
    fun datePickerDialogState(
        initDialogOpen: Boolean = false,
        date: LocalDate? = null,
        minDate: LocalDate? = null,
        maxDate: LocalDate? = null
    ): DatePickerDialogState {
        val d = remember {
            mutableStateOf(
                date ?: LocalDate.now()
            )
        }
        return DatePickerDialogState(
            dialogOpen = remember {
                mutableStateOf(initDialogOpen)
            },
            date = d,
            minDate = remember {
                mutableStateOf(minDate)
            },
            maxDate = remember {
                mutableStateOf(maxDate)
            }
        )
    }

    //companion object: GlobalScope
}


package org.tty.dailyset.data.scope

import androidx.compose.runtime.*
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.common.observable.state
import org.tty.dailyset.model.lifetime.DatePickerDialogState
import org.tty.dailyset.provider.LocalMainViewModel
import org.tty.dailyset.ui.page.MainPageTabs
import org.tty.dailyset.viewmodel.MainViewModel
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
    fun rootService(): DailySetApplication {
        return vm.service
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


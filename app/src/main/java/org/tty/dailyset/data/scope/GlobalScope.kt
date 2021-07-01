package org.tty.dailyset.data.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.tty.dailyset.model.lifetime.DatePickerDialogState
import org.tty.dailyset.viewmodel.MainViewModel
import org.tty.dailyset.provider.LocalMainViewModel
import java.time.LocalDate

@Immutable
interface GlobalScope  {
    @Composable
    fun mainViewModel(): MainViewModel {
        return LocalMainViewModel.current
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


package org.tty.dailyset.data.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import org.tty.dailyset.model.lifetime.SelectState
import org.tty.dailyset.model.lifetime.WeekDaySelectState
import org.tty.dailyset.provider.LocalMainViewModel
import org.tty.dailyset.viewmodel.MainViewModel

@Immutable
interface WeekDaySelectStateScope {
    @Composable
    fun weekDaySelectState(): WeekDaySelectState {
        val list = mutableStateListOf<SelectState>()
        list.addAll((0..7).map {
            SelectState(selected = false, enabled = true)
        })
        return WeekDaySelectState(
            _stateList = list
        )
    }

    //companion object: WeekDaySelectStateScope
}
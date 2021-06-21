package org.tty.dailyset.data.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.tty.dailyset.model.entity.DailySetType
import org.tty.dailyset.model.lifetime.dailyset.DailySetCreateState

interface DailySetScope: PreferenceScope, UserScope {
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

    companion object {
        const val TAG = "DailySetScope"
    }
}
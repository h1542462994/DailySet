package org.tty.dailyset.data.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.tty.dailyset.model.entity.DailyDuration
import org.tty.dailyset.model.entity.DailySet
import org.tty.dailyset.model.entity.DailySetType
import org.tty.dailyset.model.lifetime.dailyset.DailySetCreateState

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
    fun currentDailySet(): State<DailySet> {
        val mainViewModel = mainViewModel()
        return mainViewModel.currentDailySet.observeAsState(initial = DailySet.empty())
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

    companion object {
        const val TAG = "DailySetScope"
    }
}
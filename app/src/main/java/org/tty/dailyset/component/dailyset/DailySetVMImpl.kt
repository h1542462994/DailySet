package org.tty.dailyset.component.dailyset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.entity.DailySetIcon
import org.tty.dailyset.bean.entity.DailySetType
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.asActivityColdStateFlow
import org.tty.dailyset.component.common.sharedComponents0

@Composable
fun rememberDailySetVM(): DailySetVM {
    val sharedComponents = sharedComponents0()
    return remember {
        DailySetVMImpl(sharedComponents)
    }
}

class DailySetVMImpl(private val sharedComponents: SharedComponents) : DailySetVM {
    override val dailySets: StateFlow<List<DailySet>> =
        sharedComponents.stateStore.dailySets.asActivityColdStateFlow(listOf())

    override fun setCurrentDailySetUid(dailySetUid: String) {
        sharedComponents.stateStore.currentDailySetUid.tryEmit(dailySetUid)
    }

    override val dailySetCreateDialogVM: DailySetCreateDialogVM = object : DailySetCreateDialogVM {
        override val dialogOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
        override val selectIcon: MutableStateFlow<Boolean> =  MutableStateFlow(false)
        override val type: MutableStateFlow<DailySetType> = MutableStateFlow(DailySetType.Normal)
        override val icon: MutableStateFlow<DailySetIcon?> = MutableStateFlow(null)
        override val name: MutableStateFlow<String> = MutableStateFlow("")
        override val currentUserUid: StateFlow<String> = sharedComponents.stateStore.currentUserUid.asActivityColdStateFlow(User.local)

        override fun createDailySet(dailySetName: String, type: DailySetType, icon: DailySetIcon?) {
            val scope = sharedComponents.applicationScope
            scope.launch {
                sharedComponents.repositoryCollection.dailySetRepository.createDailySet(dailySetName, type, icon)
                dialogOpen.emit(false)
            }
        }
    }


}
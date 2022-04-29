package org.tty.dailyset.component.dailyset

import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.entity.DailySetIcon
import org.tty.dailyset.bean.entity.DailySetType
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.common.observable.InitialFlow
import org.tty.dailyset.common.observable.InitialMutableStateFlow
import org.tty.dailyset.common.observable.initial
import org.tty.dailyset.common.observable.initialMutableSharedFlowOf
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.sharedComponents0

@Composable
fun dailySetVM(): DailySetVM {
    val sharedComponents = sharedComponents0()
    return DailySetVMImpl(sharedComponents)
}

class DailySetVMImpl(private val sharedComponents: SharedComponents) : DailySetVM {
    override val dailySets: InitialFlow<List<DailySet>> =
        sharedComponents.stateStore.dailySets.initial(
            listOf()
        )

    override fun setCurrentDailySetUid(dailySetUid: String) {
        sharedComponents.stateStore.currentDailySetUid.tryEmit(dailySetUid)
    }

    override val dailySetCreateDialogVM: DailySetCreateDialogVM = object : DailySetCreateDialogVM {
        override val dialogOpen: InitialMutableStateFlow<Boolean> =
            initialMutableSharedFlowOf(false)
        override val selectIcon: InitialMutableStateFlow<Boolean> =
            initialMutableSharedFlowOf(false)
        override val type: InitialMutableStateFlow<DailySetType> =
            initialMutableSharedFlowOf(DailySetType.Normal)
        override val icon: InitialMutableStateFlow<DailySetIcon?> =
            initialMutableSharedFlowOf(null)
        override val name: InitialMutableStateFlow<String> = initialMutableSharedFlowOf("")
        override val currentUserUid: InitialFlow<String> = sharedComponents.stateStore.currentUserUid.initial(
            User.local)

        override fun createDailySet(dailySetName: String, type: DailySetType, icon: DailySetIcon?) {
            val scope = sharedComponents.applicationScope
            scope.launch {
                sharedComponents.repositoryCollection.dailySetRepository.createDailySet(dailySetName, type, icon)
                dialogOpen.emit(false)
            }
        }
    }


}
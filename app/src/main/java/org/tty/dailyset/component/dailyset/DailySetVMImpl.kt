package org.tty.dailyset.component.dailyset

import androidx.compose.runtime.Composable
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.entity.DailySetIcon
import org.tty.dailyset.bean.entity.DailySetType
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.common.observable.InitialFlow
import org.tty.dailyset.common.observable.InitialMutableSharedFlow
import org.tty.dailyset.common.observable.initial
import org.tty.dailyset.common.observable.initialMutableSharedFlowOf
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.sharedComponents

@Composable
fun dailySetVM(): DailySetVM {
    val sharedComponents = sharedComponents()
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
        override val dialogOpen: InitialMutableSharedFlow<Boolean> =
            initialMutableSharedFlowOf(false)
        override val selectIcon: InitialMutableSharedFlow<Boolean> =
            initialMutableSharedFlowOf(false)
        override val type: InitialMutableSharedFlow<DailySetType> =
            initialMutableSharedFlowOf(DailySetType.Normal)
        override val icon: InitialMutableSharedFlow<DailySetIcon?> =
            initialMutableSharedFlowOf(null)
        override val name: InitialMutableSharedFlow<String> = initialMutableSharedFlowOf("")
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
package org.tty.dailyset.component.dailyset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.bean.enums.DailySetIcon
import org.tty.dailyset.bean.enums.DailySetType
import org.tty.dailyset.bean.lifetime.DailySetSummary
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.common.observable.flowMulti
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.asAppStateFlow
import org.tty.dailyset.component.common.sharedComponents0

@Composable
@UseComponent
fun rememberDailySetVM(): DailySetVM {
    val sharedComponents = sharedComponents0()
    return remember {
        DailySetVMImpl(sharedComponents)
    }
}

class DailySetVMImpl(private val sharedComponents: SharedComponents) : DailySetVM {
    override val dailySetSummaries: StateFlow<List<DailySetSummary>> = produceDailySetSummariesFlow().asAppStateFlow(
        listOf())

    override fun setCurrentDailySetUid(dailySetUid: String) {
        sharedComponents.stateStore.currentDailySetUid.tryEmit(dailySetUid)
    }

    override val dailySetCreateDialogVM: DailySetCreateDialogVM = object : DailySetCreateDialogVM {
        override val dialogOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
        override val selectIcon: MutableStateFlow<Boolean> =  MutableStateFlow(false)
        override val type: MutableStateFlow<DailySetType> = MutableStateFlow(DailySetType.Normal)
        override val icon: MutableStateFlow<DailySetIcon?> = MutableStateFlow(null)
        override val name: MutableStateFlow<String> = MutableStateFlow("")
        override val currentUserUid: StateFlow<String> = sharedComponents.stateStore.currentUserUid

//        override fun createDailySet(dailySetName: String, type: DailySetType, icon: DailySetIcon?) {
//            val scope = sharedComponents.applicationScope
//            scope.launch {
//                sharedComponents.actorCollection.dailySetRepository.createDailySet(dailySetName, type, icon)
//                dialogOpen.emit(false)
//            }
//        }
    }

    private fun produceDailySetSummariesFlow(): Flow<List<DailySetSummary>> {
        // watch the following flow to create the source of the flow.
        // 这样写是为了让最终的流能够监听下面4个流的push.
        val comp1 = sharedComponents.database.dailySetDao().anyFlow()
        val comp2 = sharedComponents.database.dailySetVisibleDao().anyFlow()
        val comp3 = sharedComponents.database.dailySetMetaLinksDao().anyFlow()
        val comp4 = sharedComponents.database.dailySetBasicMetaDao().anyFlow()

        // 由于对于这个复杂的对象应用流转换非常复杂，因此采用了当设计的数据表更改时，重新请求数据的方案。
        return flowMulti(comp1, comp2, comp3, comp4) {
             return@flowMulti withContext(Dispatchers.IO) {
                 logger.d("DailySetClazzAutoVMImpl", "???")
                 return@withContext sharedComponents.actorCollection.dailySetActor.getDailySetSummaries()
             }
        }
    }

}
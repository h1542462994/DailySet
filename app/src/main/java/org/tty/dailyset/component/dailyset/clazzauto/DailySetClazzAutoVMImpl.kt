package org.tty.dailyset.component.dailyset.clazzauto

import androidx.compose.runtime.Composable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.tty.dailyset.bean.entity.DailySetDuration
import org.tty.dailyset.bean.entity.DefaultEntities
import org.tty.dailyset.bean.enums.DailySetClazzAutoViewType
import org.tty.dailyset.bean.lifetime.DailySetClazzAutoPageInfo
import org.tty.dailyset.bean.lifetime.DailySetSummary
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.common.observable.flowMulti
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.asActivityColdStateFlow
import org.tty.dailyset.component.common.sharedComponents

@Composable
fun rememberClazzAutoDailySetVM(dailySetUid: String): DailySetClazzAutoVM {
    val sharedComponents = sharedComponents()
    return sharedComponents.ltsVMSaver.getVM("dailyset/clazzauto/${dailySetUid}") {
        DailySetClazzAutoVMImpl(sharedComponents, dailySetUid)
    }
}

class DailySetClazzAutoVMImpl(val sharedComponents: SharedComponents, val dailySetUid: String): DailySetClazzAutoVM {
    override val dailySetSummary: StateFlow<DailySetSummary> = produceDailySetSummaryFlow().asActivityColdStateFlow(DefaultEntities.emptyDailySetSummary())
    override val dailySetClazzAutoViewType: MutableStateFlow<DailySetClazzAutoViewType> = MutableStateFlow(DailySetClazzAutoViewType.Week)
    override val dailySetClazzAutoPageInfos: StateFlow<List<DailySetClazzAutoPageInfo>> = produceDailySetClazzAutoPagerInfosFlow().asActivityColdStateFlow(listOf())
    override val dailySetCurrentPageIndex = MutableStateFlow(-1)

    private fun produceDailySetSummaryFlow(): Flow<DailySetSummary> {
        val comp1 = sharedComponents.database.dailySetDao().anyFlow()
        val comp2 = sharedComponents.database.dailySetVisibleDao().anyFlow()
        val comp3 = sharedComponents.database.dailySetMetaLinksDao().anyFlow()
        val comp4 = sharedComponents.database.dailySetBasicMetaDao().anyFlow()

        return flowMulti(comp1, comp2, comp3, comp4) {
            return@flowMulti withContext(Dispatchers.IO) {
                logger.d("DailySetClazzAutoVMImpl", "??")
                return@withContext sharedComponents.actorCollection.dailySetActor.getDailySetSummary(dailySetUid = dailySetUid)!!
            }
        }
    }

    private fun produceDailySetDurationFlow(): Flow<List<DailySetDuration>> {
        val comp1 = sharedComponents.database.dailySetDao().anyFlow()
        val comp2 = sharedComponents.database.dailySetSourceLinksDao().anyFlow()
        val comp3 = sharedComponents.database.dailySetDurationDao().anyFlow()

        return flowMulti(comp1, comp2, comp3) {
            withContext(Dispatchers.IO) {
                sharedComponents.actorCollection.dailySetActor.getDailySetDurations(dailySetUid = dailySetUid)
            }
        }
    }

    private fun produceDailySetClazzAutoPagerInfosFlow(): Flow<List<DailySetClazzAutoPageInfo>> {
        return produceDailySetDurationFlow().map {
            val durations = DailySetClazzAutoPageInfo.fromDurations(it)
            durations.forEach { info ->
                logger.d("debug", info.toString())
            }

            durations
        }
    }
}
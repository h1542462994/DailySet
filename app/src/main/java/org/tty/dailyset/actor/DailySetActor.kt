package org.tty.dailyset.actor

import org.tty.dailyset.bean.ResponseCodes
import org.tty.dailyset.bean.entity.*
import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.common.util.Diff
import org.tty.dailyset.component.common.SharedComponents
import java.util.*

/**
 * repository for [DailySetTable],[DailySetRow],[DailySetCell]
 * it is used in [org.tty.dailyset.DailySetApplication],
 * it will use db service, see also [org.tty.dailyset.database.DailySetRoomDatabase]
 */
class DailySetActor(private val sharedComponents: SharedComponents) {

    suspend fun updateData() {
        doUpdateData()
    }

    private suspend fun doUpdateData() {
        val userUid: String = sharedComponents.actorCollection.preferenceActor.read(PreferenceName.CURRENT_USER_UID)

        val responseDailySetInfos = sharedComponents.dataSourceCollection.netSourceCollection.dailySetService.dailySetInfo()
        if (responseDailySetInfos.code == ResponseCodes.success) {
            if (responseDailySetInfos.data != null) {
                withDailySetInfos(responseDailySetInfos.data, userUid)
            }
        }
    }

    private suspend fun withDailySetInfos(dailySets: List<DailySet>, userUid: String) {
        val dailySetVisibles = sharedComponents.database.dailySetVisibleDao().allByUserUid(userUid)
        val diff = Diff<DailySetVisible, DailySet, String> {
            source = dailySetVisibles
            target = dailySets
            sourceKeySelector = { it.dailySetUid }
            targetKeySelector = { it.uid }
        }
    }

}
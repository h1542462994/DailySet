package org.tty.dailyset.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.*
import org.tty.dailyset.common.uuid
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.database.processor.DailySetProcessor2Async
import org.tty.dailyset.event.DailySetBindingDurationEventArgs
import org.tty.dailyset.event.DailySetCreateDurationAndBindingEventArgs
import org.tty.dailyset.event.DailySetCreateEventArgs
import java.time.LocalDateTime

class DailyRepository(private val sharedComponents: SharedComponents): DailySetProcessor2Async {
    private val dailySetDao get() = sharedComponents.dataSourceCollection.dbSourceCollection.dailySetDao
    private val dailyDurationDao get() = sharedComponents.dataSourceCollection.dbSourceCollection.dailyDurationDao
    private val dailySetBindingDao get() = sharedComponents.dataSourceCollection.dbSourceCollection.dailySetBindingDao

    val dailySets: Flow<List<DailySet>> = dailySetDao.allSets()
    val normalDailyDurations: Flow<List<DailyDuration>> = dailyDurationDao.typedDurations(type = DailyDurationType.Normal)
    val clazzDailyDurations: Flow<List<DailyDuration>> = dailyDurationDao.typedDurations(type = DailyDurationType.Clazz)


    fun loadDailySet(dailySetUid: String): Flow<DailySet?> {
        return dailySetDao.load(dailySetUid)
    }

    fun loadDailySetDurations(dailySetUid: String): Flow<DailySetDurations?> {
        return dailySetDao.loadDetail(dailySetUid)
    }

    fun loadDailySetBinding(dailySetUid: String, dailyDurationUid: String): Flow<DailySetBinding?> {
        return dailySetBindingDao.loadDailySetBinding(dailySetUid, dailyDurationUid)
    }

    override suspend fun create(dailySetCreateEventArgs: DailySetCreateEventArgs) {
        dailySetDao.create(dailySetCreateEventArgs)
    }

    override suspend fun createDuration(dailySetCreateDurationAndBindingEventArgs: DailySetCreateDurationAndBindingEventArgs) {
        dailySetDao.createDuration(dailySetCreateDurationAndBindingEventArgs)
    }

    override suspend fun bindingDuration(dailySetBindingDurationEventArgs: DailySetBindingDurationEventArgs) {
        dailySetDao.bindingDuration(dailySetBindingDurationEventArgs)
    }

    suspend fun createDailySet(dailySetName: String, type: DailySetType, icon: DailySetIcon?) {
        sharedComponents.database.withTransaction {
            val serialIndex = dailySetDao.countOfType(type)
            val uid = uuid()
            val dailySet = DailySet(
                type = type,
                icon = icon,
                uid = uid,
                serialIndex = serialIndex,
                ownerUid = sharedComponents.stateStore.currentUserUidSnapshot,
                name = dailySetName,
                updateAt = LocalDateTime.now()
            )
            dailySetDao.update(dailySet)
        }
    }
}
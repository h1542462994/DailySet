package org.tty.dailyset.data.repository

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.data.processor.DailySetProcessor2Async
import org.tty.dailyset.event.DailySetBindingDurationEventArgs
import org.tty.dailyset.event.DailySetCreateDurationAndBindingEventArgs
import org.tty.dailyset.event.DailySetCreateEventArgs
import org.tty.dailyset.model.dao.DailyDurationDao
import org.tty.dailyset.model.dao.DailySetBindingDao
import org.tty.dailyset.model.dao.DailySetDao
import org.tty.dailyset.model.entity.*

class DailySetRepository(
    private val dailySetDao: DailySetDao,
    private val dailyDurationDao: DailyDurationDao,
    private val dailySetBindingDao: DailySetBindingDao
): DailySetProcessor2Async {
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
}
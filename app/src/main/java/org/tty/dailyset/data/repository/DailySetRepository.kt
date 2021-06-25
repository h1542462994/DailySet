package org.tty.dailyset.data.repository

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.data.processor.DailySetProcessor2Async
import org.tty.dailyset.event.DailySetCreateEventArgs
import org.tty.dailyset.model.dao.DailyDurationDao
import org.tty.dailyset.model.dao.DailySetDao
import org.tty.dailyset.model.entity.DailyDuration
import org.tty.dailyset.model.entity.DailyDurationType
import org.tty.dailyset.model.entity.DailySet
import org.tty.dailyset.model.entity.DailySetDurations

class DailySetRepository(
    private val dailySetDao: DailySetDao,
    private val dailyDurationDao: DailyDurationDao,
): DailySetProcessor2Async {
    val dailySets: Flow<List<DailySet>> = dailySetDao.all()
    val normalDailyDurations: Flow<List<DailyDuration>> = dailyDurationDao.typedDurations(type = DailyDurationType.Normal)
    val clazzDailyDurations: Flow<List<DailyDuration>> = dailyDurationDao.typedDurations(type = DailyDurationType.Clazz)

    fun loadDailySet(dailySetUid: String): Flow<DailySet?> {
        return dailySetDao.load(dailySetUid)
    }

    fun loadDailySetDurations(dailySetUid: String): Flow<DailySetDurations?> {
        return dailySetDao.loadDetail(dailySetUid)
    }

    override suspend fun create(dailySetCreateEventArgs: DailySetCreateEventArgs) {
        dailySetDao.create(dailySetCreateEventArgs)
    }
}
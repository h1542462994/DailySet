package org.tty.dailyset.data.repository

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.data.processor.DailySetProcessor2Async
import org.tty.dailyset.event.DailySetCreateEventArgs
import org.tty.dailyset.model.dao.DailySetDao
import org.tty.dailyset.model.entity.DailySet

class DailySetRepository(
    private val dailySetDao: DailySetDao
): DailySetProcessor2Async {
    val dailySets: Flow<List<DailySet>> = dailySetDao.all()

    fun loadDailySet(dailySetUid: String): Flow<DailySet?> {
        return dailySetDao.load(dailySetUid)
    }

    override suspend fun create(dailySetCreateEventArgs: DailySetCreateEventArgs) {
        dailySetDao.create(dailySetCreateEventArgs)
    }
}
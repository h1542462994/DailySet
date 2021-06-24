package org.tty.dailyset.data.repository

import org.tty.dailyset.data.processor.DailySetProcessor2Async
import org.tty.dailyset.event.DailySetCreateEventArgs
import org.tty.dailyset.model.dao.DailySetDao

class DailySetRepository(
    private val dailySetDao: DailySetDao
): DailySetProcessor2Async {
    override suspend fun create(dailySetCreateEventArgs: DailySetCreateEventArgs) {
        dailySetDao.create(dailySetCreateEventArgs)
    }
}
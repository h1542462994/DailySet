package org.tty.dailyset.data.repository

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.dao.DailyCellDao
import org.tty.dailyset.model.dao.DailyRowDao
import org.tty.dailyset.model.dao.DailyTableDao
import org.tty.dailyset.model.entity.DailyTable

class DailyTableRepository(
    private val dailyTableDao: DailyTableDao,
    private val dailyRowDao: DailyRowDao,
    private val dailyCellDao: DailyCellDao) {
    val dailyTableSummaries: Flow<List<DailyTable>> = dailyTableDao.all()
}
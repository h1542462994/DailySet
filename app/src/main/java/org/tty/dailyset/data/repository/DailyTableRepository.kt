package org.tty.dailyset.data.repository

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.data.processor.DailyTableProcessor2Async
import org.tty.dailyset.event.*
import org.tty.dailyset.model.dao.DailyCellDao
import org.tty.dailyset.model.dao.DailyRowDao
import org.tty.dailyset.model.dao.DailyTableDao
import org.tty.dailyset.model.entity.*
import java.util.*

/**
 * repository for [DailyTable],[DailyRow],[DailyCell]
 * it is used in [org.tty.dailyset.DailySetApplication],
 * it will use db service, see also [org.tty.dailyset.data.DailySetRoomDatabase]
 */
class DailyTableRepository(
    private val dailyTableDao: DailyTableDao,
    private val dailyRowDao: DailyRowDao,
    private val dailyCellDao: DailyCellDao): DailyTableProcessor2Async {
    val dailyTableSummaries: Flow<List<DailyTable>> = dailyTableDao.all()
    fun loadDailyTRC(dailyTableUid: String): Flow<DailyTRC?> {
        return dailyTableDao.loadSorted(dailyTableUid)
    }
    @Deprecated("use loadDailyTRC")
    fun getDailyTRC(dailyTableUid: String): DailyTRC? {
        return dailyTableDao.get(dailyTableUid)
    }

    /**
     * create DailyTable from template
     * db operation function, see also [org.tty.dailyset.data.scope.DailyTableScope.dailyTableCreateFromTemplate]
     */
    override suspend fun createFromTemplate(dailyTableCreateEventArgs: DailyTableCreateEventArgs) {
        dailyTableDao.createFromTemplate(dailyTableCreateEventArgs)
    }

    /**
     * delete DailyTable
     * db operation function, see also [org.tty.dailyset.data.scope.DailyTableScope.dailyTableDelete]
     */
    override suspend fun delete(dailyTableDeleteEventArgs: DailyTableDeleteEventArgs) {
        dailyTableDao.delete(dailyTableDeleteEventArgs)
    }

    /**
     * addRow from the DailyTable
     */
    override suspend fun addRow(dailyTableAddRowEventArgs: DailyTableAddRowEventArgs) {
        dailyTableDao.addRow(dailyTableAddRowEventArgs)
    }

    override suspend fun clickWeekDay(dailyTableClickWeekDayEventArgs: DailyTableClickWeekDayEventArgs) {
        dailyTableDao.clickWeekDay(dailyTableClickWeekDayEventArgs)
    }

    override suspend fun rename(dailyTableRenameEventArgs: DailyTableRenameEventArgs) {
        dailyTableDao.rename(dailyTableRenameEventArgs)
    }

    override suspend fun deleteRow(dailyTableRowDeleteEventArgs: DailyTableRowDeleteEventArgs) {
        dailyTableDao.deleteRow(dailyTableRowDeleteEventArgs)
    }


}
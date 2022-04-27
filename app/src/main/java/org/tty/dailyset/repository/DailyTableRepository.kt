package org.tty.dailyset.repository

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.database.processor.DailyTableProcessor2Async
import org.tty.dailyset.event.*
import org.tty.dailyset.datasource.db.DailyCellDao
import org.tty.dailyset.datasource.db.DailyRowDao
import org.tty.dailyset.datasource.db.DailyTableDao
import org.tty.dailyset.bean.entity.*
import org.tty.dailyset.component.common.SharedComponents
import java.util.*

/**
 * repository for [DailyTable],[DailyRow],[DailyCell]
 * it is used in [org.tty.dailyset.DailySetApplication],
 * it will use db service, see also [org.tty.dailyset.database.DailySetRoomDatabase]
 */
class DailyTableRepository(private val sharedComponents: SharedComponents) : DailyTableProcessor2Async {
    private val dailyTableDao get() = sharedComponents.dataSourceCollection.dbSourceCollection.dailyTableDao

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
     * db operation function, see also [org.tty.dailyset.database.scope.DailyTableScope.dailyTableCreateFromTemplate]
     */
    override suspend fun createFromTemplate(dailyTableCreateEventArgs: DailyTableCreateEventArgs) {
        dailyTableDao.createFromTemplate(dailyTableCreateEventArgs)
    }

    /**
     * delete DailyTable
     * db operation function, see also [org.tty.dailyset.database.scope.DailyTableScope.dailyTableDelete]
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

    override suspend fun modifySection(dailyTableModifySectionEventArgs: DailyTableModifySectionEventArgs) {
        dailyTableDao.modifySection(dailyTableModifySectionEventArgs)
    }

    override suspend fun modifyCell(dailyTableModifyCellEventArgs: DailyTableModifyCellEventArgs) {
        dailyTableDao.modifyCell(dailyTableModifyCellEventArgs)
    }

}
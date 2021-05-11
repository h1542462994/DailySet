package org.tty.dailyset.data.repository

import androidx.room.RoomDatabase
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.dao.DailyCellDao
import org.tty.dailyset.model.dao.DailyRowDao
import org.tty.dailyset.model.dao.DailyTableDao
import org.tty.dailyset.model.entity.*
import org.tty.dailyset.ui.utils.localTimestampNow
import java.util.*

/**
 * repository for [DailyTable],[DailyRow],[DailyCell]
 * it is used in [org.tty.dailyset.DailySetApplication],
 * it will use db service, see also [org.tty.dailyset.data.DailySetRoomDatabase]
 */
class DailyTableRepository(
    private val dailyTableDao: DailyTableDao,
    private val dailyRowDao: DailyRowDao,
    private val dailyCellDao: DailyCellDao) {
    val dailyTableSummaries: Flow<List<DailyTable>> = dailyTableDao.all()
    fun loadDailyTRC(dailyTableUid: String): Flow<DailyTRC?> {
        return dailyTableDao.load(dailyTableUid)
    }
    fun getDailyTRC(dailyTableUid: String): DailyTRC? {
        return dailyTableDao.get(dailyTableUid)
    }

    /**
     * create DailyTable from template
     * db operation function, see also [org.tty.dailyset.data.scope.DailyTableScope.dailyTableCreateFromTemplate]
     */
    suspend fun createFromTemplate(name: String, cloneFrom: DailyTable, uid: String, referenceUid: String) {
        dailyTableDao.createFromTemplate(name, cloneFrom, uid, referenceUid)
    }

    /**
     * delete DailyTable
     * db operation function, see also [org.tty.dailyset.data.scope.DailyTableScope.dailyTableDelete]
     */
    suspend fun delete(dailyTRC: DailyTRC) {
        dailyTableDao.delete(dailyTRC)
    }

    /**
     * addRow from the DailyTable
     */
    suspend fun addRow(dailyTRC: DailyTRC, weekDays: IntArray) {
        dailyTableDao.addRow(dailyTRC, weekDays)
    }


}
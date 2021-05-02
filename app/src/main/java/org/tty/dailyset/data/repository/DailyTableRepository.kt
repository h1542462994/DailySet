package org.tty.dailyset.data.repository

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
    @Transaction
    suspend fun createFromTemplate(name: String, cloneFrom: DailyTable, uid: String, referenceUid: String) {
        val dailyTRC = dailyTableDao.get(cloneFrom.uid)
        if (dailyTRC != null) {
            val newDailyTable = dailyTRC.dailyTable.copy(
                name = name,
                uid = uid,
                referenceUid = referenceUid,
                updateAt = localTimestampNow(),
                global = false
            )
            dailyTableDao.update(newDailyTable)
            dailyTRC.dailyRCs.forEach { dailyRC ->
                val newDailyRow = dailyRC.dailyRow.copy(
                    uid = UUID.randomUUID().toString(),
                    updateAt = localTimestampNow(),
                    dailyTableUid = uid
                )
                dailyRowDao.update(newDailyRow)
                dailyRC.dailyCells.forEach { dailyCell ->
                    val newDailyCell = dailyCell.copy(
                        uid = UUID.randomUUID().toString(),
                        updateAt = localTimestampNow(),
                        dailyRowUid = newDailyRow.uid
                    )
                    dailyCellDao.update(newDailyCell)
                }
            }
        }


    }

    /**
     * delete DailyTable
     * db operation function, see also [org.tty.dailyset.data.scope.DailyTableScope.dailyTableDelete]
     */
    @Transaction
    suspend fun delete(dailyTRC: DailyTRC) {
        dailyTRC.dailyRCs.forEach { dailyRC ->
            dailyRC.dailyCells.forEach { dailyCell ->
                // delete the dailyCell
                dailyCellDao.delete(dailyCell = dailyCell)
            }
            dailyRowDao.delete(dailyRow = dailyRC.dailyRow)
        }
        dailyTableDao.delete(dailyTable = dailyTRC.dailyTable)
    }

    /**
     * addRow from the DailyTable
     */
    @Transaction
    suspend fun addRow(dailyTRC: DailyTRC, weekDays: IntArray) {
        val copyFrom = dailyTRC.dailyRCs.last()
        val copyFromWeekDays = copyFrom.dailyRow.weekdays.subtract(weekDays.toList()).toIntArray()

        // update the copyFrom
        copyFrom.dailyRow.weekdays = copyFromWeekDays
        dailyRowDao.update(copyFrom.dailyRow)

        // insert the new row
        val dailyRowUid = UUID.randomUUID().toString()
        val newDailyRC = DailyRC(
            dailyRow = copyFrom.dailyRow.copy(
                uid = dailyRowUid,
                weekdays = weekDays,
                updateAt = localTimestampNow()
            ),
            dailyCells = copyFrom.dailyCells.map {
                it.copy(
                    uid = UUID.randomUUID().toString(),
                    dailyRowUid = dailyRowUid,
                    updateAt = localTimestampNow()
                )
            }
        )
        dailyRowDao.update(newDailyRC.dailyRow)
        newDailyRC.dailyCells.forEach {
            dailyCellDao.update(it)
        }
    }
}
package org.tty.dailyset.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.entity.DailyRC
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.ui.utils.localTimestampNow
import java.util.*

@Dao
interface DailyTableDao : DailyRowDao, DailyCellDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailyTable: DailyTable)

    @Transaction
    @Query("SELECT * FROM daily_table WHERE uid = :uid LIMIT 1")
    fun get(uid: String): DailyTRC?

    @Transaction
    @Query("SELECT * FROM daily_table WHERE uid = :uid LIMIT 1")
    fun load(uid: String): Flow<DailyTRC?>

    @Query("SELECT * FROM daily_table")
    fun all(): Flow<List<DailyTable>>

    @Delete
    fun delete(dailyTable: DailyTable)


    /**
     * create DailyTable from template
     * db operation function, see also [org.tty.dailyset.data.scope.DailyTableScope.dailyTableCreateFromTemplate]
     */
    @Transaction
    suspend fun createFromTemplate(name: String, cloneFrom: DailyTable, uid: String, referenceUid: String) {
        val dailyTRC = get(cloneFrom.uid)
        if (dailyTRC != null) {
            val newDailyTable = dailyTRC.dailyTable.copy(
                name = name,
                uid = uid,
                referenceUid = referenceUid,
                updateAt = localTimestampNow(),
                global = false
            )
            update(newDailyTable)
            dailyTRC.dailyRCs.forEach { dailyRC ->
                val newDailyRow = dailyRC.dailyRow.copy(
                    uid = UUID.randomUUID().toString(),
                    updateAt = localTimestampNow(),
                    dailyTableUid = uid
                )
                update(newDailyRow)
                dailyRC.dailyCells.forEach { dailyCell ->
                    val newDailyCell = dailyCell.copy(
                        uid = UUID.randomUUID().toString(),
                        updateAt = localTimestampNow(),
                        dailyRowUid = newDailyRow.uid
                    )
                    update(newDailyCell)
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
                delete(dailyCell = dailyCell)
            }
            delete(dailyRow = dailyRC.dailyRow)
        }
        delete(dailyTable = dailyTRC.dailyTable)
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
        update(copyFrom.dailyRow)

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
        update(newDailyRC.dailyRow)
        newDailyRC.dailyCells.forEach {
            update(it)
        }

        update(
            dailyTRC.dailyTable.copy(
                updateAt = localTimestampNow()
            )
        )
    }


}
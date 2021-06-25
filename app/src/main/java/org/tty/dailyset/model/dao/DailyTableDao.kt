package org.tty.dailyset.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tty.dailyset.data.processor.DailyTableProcessor2Async
import org.tty.dailyset.event.*
import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyRC
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.localTimestampNow
import org.tty.dailyset.plus
import org.tty.dailyset.spanMinutes
import java.time.temporal.ChronoUnit
import java.util.*

@Dao
interface DailyTableDao : DailyRowDao, DailyCellDao, DailyTableProcessor2Async {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailyTable: DailyTable)

    @Transaction
    @Query("SELECT * FROM daily_table WHERE uid = :uid LIMIT 1")
    @Deprecated("use load")
    fun get(uid: String): DailyTRC?

    @Transaction
    @Query("SELECT * FROM daily_table WHERE uid = :uid LIMIT 1")
    fun load(uid: String): Flow<DailyTRC?>

    fun loadSorted(uid: String): Flow<DailyTRC?> {
        val value = load(uid)
        return value.map { it ->
            if (it == null) {
                return@map it
            } else {
                return@map it.copy(
                    dailyRCs = it.dailyRCs.map { it2 ->
                        it2.copy(
                            dailyCells = it2.dailyCells.sortedBy { it3 -> it3.currentIndex }
                        )
                    }.sortedBy { it.dailyRow.currentIndex }
                )
            }
        }
    }

    @Query("SELECT * FROM daily_table")
    fun all(): Flow<List<DailyTable>>

    @Delete
    fun delete(dailyTable: DailyTable)

    /**
     * create DailyTable from template
     * db operation function, see also [org.tty.dailyset.data.scope.DailyTableScope.dailyTableCreateFromTemplate]
     */
    @Transaction
    override suspend fun createFromTemplate(dailyTableCreateEventArgs: DailyTableCreateEventArgs) {
        val (name, cloneFrom, uid, referenceUid) = dailyTableCreateEventArgs
        val newDailyTable = cloneFrom.dailyTable.copy(
            name = name,
            uid = uid,
            referenceUid = referenceUid,
            updateAt = localTimestampNow(),
            global = false
        )
        update(newDailyTable)
        cloneFrom.dailyRCs.forEach { dailyRC ->
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

    /**
     * delete DailyTable
     * db operation function, see also [org.tty.dailyset.data.scope.DailyTableScope.dailyTableDelete]
     */
    @Transaction
    override suspend fun delete(dailyTableDeleteEventArgs: DailyTableDeleteEventArgs) {
        val (dailyTRC) = dailyTableDeleteEventArgs
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
    override suspend fun addRow(dailyTableAddRowEventArgs: DailyTableAddRowEventArgs) {
        val (dailyTRC, weekDays) = dailyTableAddRowEventArgs
        val copyFrom = dailyTRC.dailyRCs.last()
        val copyFromWeekDays = copyFrom.dailyRow.weekdays.subtract(weekDays.toList()).toIntArray()

        // update the copyFrom
        copyFrom.dailyRow.weekdays = copyFromWeekDays
        update(copyFrom.dailyRow)

        // insert the new row
        val dailyRowUid = UUID.randomUUID().toString()
        val currentIndex = dailyTRC.dailyRCs.count()
        val newDailyRC = DailyRC(
            dailyRow = copyFrom.dailyRow.copy(
                uid = dailyRowUid,
                currentIndex = currentIndex,
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

    /**
     * clickWeekDay of DailyTable
     */
    @Transaction
    override suspend fun clickWeekDay(dailyTableClickWeekDayEventArgs: DailyTableClickWeekDayEventArgs) {
        val (dailyTRC, rowIndex, weekDay) = dailyTableClickWeekDayEventArgs
        //assert(weekDay in 1..7)
        fun dailyRowOfRowIndex(index: Int) = dailyTRC.dailyRCs[index].dailyRow
        fun weekDayOfRowIndex(index: Int) = dailyRowOfRowIndex(index).weekdays

        val currentWeekDay = weekDayOfRowIndex(rowIndex)
        val checked = currentWeekDay.contains(weekDay)

        if (!checked) {
            for (dailyRC in dailyTRC.dailyRCs) {
                // 找到checked的一行
                val target = dailyRC.dailyRow
                if (target.weekdays.contains(weekDay)) {
                    update(
                        dailyRow = target.copy(
                            weekdays = target.weekdays.filter { it != weekDay }.toIntArray(),
                            updateAt = localTimestampNow()
                        )
                    )
                }
            }

            val source = dailyRowOfRowIndex(rowIndex)
            update(
                dailyRow = source.copy(
                    weekdays = source.weekdays.plus(weekDay).sortedArray(),
                    updateAt = localTimestampNow()
                )
            )
        } else {
            val source = dailyRowOfRowIndex(rowIndex)
            update(
                dailyRow = source.copy(
                    weekdays = source.weekdays.filter { it != weekDay }.toIntArray(),
                    updateAt = localTimestampNow()
                )
            )
            val target = dailyRowOfRowIndex(rowIndex + 1)
            update(
                dailyRow = target.copy(
                    weekdays = target.weekdays.plus(weekDay).sortedArray(),
                    updateAt = localTimestampNow()
                )
            )
        }

        update(
            dailyTRC.dailyTable.copy(
                updateAt = localTimestampNow()
            )
        )
    }

    /**
     * rename the DailyTable
     */
    @Transaction
    override suspend fun rename(dailyTableRenameEventArgs: DailyTableRenameEventArgs) {
        val (dailyTRC, name) = dailyTableRenameEventArgs
        update(
            dailyTable = dailyTRC.dailyTable.copy(
                name = name,
                updateAt = localTimestampNow()
            )
        )
    }

    /**
     * deleteRow from the DailyTable
     */
    @Transaction
    override suspend fun deleteRow(dailyTableRowDeleteEventArgs: DailyTableRowDeleteEventArgs) {
        val (dailyTRC, rowIndex) = dailyTableRowDeleteEventArgs
        assert(rowIndex > 0)
        fun dailyRowOfRowIndex(index: Int) = dailyTRC.dailyRCs[index].dailyRow
        val weekDays = dailyRowOfRowIndex(rowIndex - 1).weekdays
            .plus(dailyRowOfRowIndex(rowIndex).weekdays).sortedArray()
        // modify the previous
        update(
            dailyRow = dailyRowOfRowIndex(rowIndex - 1).copy(
                weekdays = weekDays,
                updateAt = localTimestampNow()
            )
        )
        // modify the next
        for (index in rowIndex + 1 until dailyTRC.dailyRCs.size) {
            val current = dailyRowOfRowIndex(index)
            update(
                dailyRow = current.copy(
                    currentIndex = current.currentIndex - 1,
                    updateAt = localTimestampNow()
                )
            )
        }
        // delete the current
        delete(dailyRowOfRowIndex(rowIndex))
        // update the dailyTable
        update(
            dailyTable = dailyTRC.dailyTable.copy(
                updateAt = localTimestampNow()
            )
        )
    }

    /**
     * modify the section of the DailyRow
     */
    @Transaction
    override suspend fun modifySection(dailyTableModifySectionEventArgs: DailyTableModifySectionEventArgs) {
        // unpack the data
        val (dailyTRC, rowIndex, counts) = dailyTableModifySectionEventArgs
        val dailyRC = dailyTRC.dailyRCs[rowIndex]
        val beforeCounts = dailyRC.dailyRow.counts
        if (beforeCounts.contentEquals(counts)) {
            // equalization
            return
        }

        // no equal
        suspend fun modifySectionOfNormalType(normalType: Int) {
            require(normalType in 0..2) {
                "normalType should in 0..2"
            }
            val mutableList =
                dailyRC.dailyCells.filter { it.normalType == normalType }.toMutableList()
            val beforeSize = mutableList.size
            val afterSize = counts[normalType]
            val startIndex = counts.filterIndexed { index, _ -> index < normalType }.sum()


            if (afterSize < beforeSize) {
                // delete the cells and modify
                mutableList.forEachIndexed { index, dailyCell ->
                    if (index < afterSize) {
                        update(
                            dailyCell.copy(
                                currentIndex = startIndex + index,
                                serialIndex = index,
                                updateAt = localTimestampNow()
                            )
                        )
                    } else {
                        // delete
                        delete(dailyCell)
                    }
                }
            } else {
                mutableList.forEachIndexed { index, dailyCell ->
                    update(
                        dailyCell.copy(
                            currentIndex = startIndex + index,
                            serialIndex = index,
                            updateAt = localTimestampNow()
                        )
                    )
                }

                // insert the new data.
                var last = mutableList.last()
                for (i in beforeSize until afterSize) {
                    val start = last.end.plus(10, ChronoUnit.MINUTES)
                    val end = start.plus(45, ChronoUnit.MINUTES)
                    val newDailyCell = DailyCell(
                        uid = UUID.randomUUID().toString(),
                        currentIndex = startIndex + i,
                        serialIndex = i,
                        start = start,
                        end = end,
                        normalType = normalType,
                        dailyRowUid = dailyRC.dailyRow.uid,
                        updateAt = localTimestampNow()
                    )
                    update(dailyCell = newDailyCell)
                    last = newDailyCell
                }
            }
        }
        modifySectionOfNormalType(0)
        modifySectionOfNormalType(1)
        modifySectionOfNormalType(2)

        update(
            dailyRC.dailyRow.copy(
                counts = counts,
                updateAt = localTimestampNow()
            )
        )
    }

    @Transaction
    override suspend fun modifyCell(dailyTableModifyCellEventArgs: DailyTableModifyCellEventArgs) {
        // unwrap the data class.
        val (dailyTRC, rowIndex, cellIndex, start, end) = dailyTableModifyCellEventArgs
        fun dailyRowOfRowIndex(rowIndex: Int) = dailyTRC.dailyRCs[rowIndex]
        fun dailyCell(rowIndex:Int, cellIndex: Int) = dailyRowOfRowIndex(rowIndex).dailyCells[cellIndex]
        val oldCell = dailyCell(rowIndex, cellIndex)
        // the offsets of the follow cells
        val spanMinutes = spanMinutes(oldCell.end, end)
        update(
            dailyCell = oldCell.copy(
                start = start,
                end = end,
                updateAt = localTimestampNow()
            )
        )
        // update follows
        val follows = (cellIndex + 1 until dailyRowOfRowIndex(rowIndex).dailyCells.size).map {
            dailyCell(rowIndex, it)
        }
        follows.forEach {
            update(
                dailyCell = it.copy(
                    start = it.start.plus(spanMinutes, ChronoUnit.MINUTES),
                    end = it.end.plus(spanMinutes, ChronoUnit.MINUTES),
                    updateAt = localTimestampNow()
                )
            )
        }
        update(
            dailyRow = dailyRowOfRowIndex(rowIndex).dailyRow.copy(
                updateAt = localTimestampNow()
            )
        )
        update(
            dailyTable = dailyTRC.dailyTable.copy(
                updateAt = localTimestampNow()
            )
        )



    }
}
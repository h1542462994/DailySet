package org.tty.dailyset.model.lifetime

import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.User

/**
 * detail of [dailyTRC], use computing.
 */
class DailyTableState2(
    val dailyTRC: DailyTRC,
    val currentUserUid: String?,
) {
    var readOnly: Boolean = true
        internal set
    var weekDays: List<List<WeekDayState>>
    var initialAddRowWeekDays: List<WeekDayState>
    var canAddRow: Boolean = false
        internal set

    init {
        this.readOnly = calcReadOnly()
        val (w, a) = calcWeekDaysAndInitialAddRowWeekDays()
        this.weekDays = w
        this.initialAddRowWeekDays = a
        this.canAddRow = calcCanAddRow()
    }

    private fun calcCanAddRow(): Boolean {
        return if (this.dailyRows.isEmpty()) {
            true
        } else {
            this.dailyRows.last().weekdays.count() > 1
        }
    }

    private val dailyTable
        get() = dailyTRC.dailyTable

    private val dailyRCs
        get() = dailyTRC.dailyRCs

    private val dailyRows
        get() = dailyRCs.map { it.dailyRow }

    private fun calcReadOnly(): Boolean {
        return dailyTable.global || dailyTable.referenceUid != currentUserUid
    }

    private fun calcWeekDaysAndInitialAddRowWeekDays(): Pair<List<List<WeekDayState>>, List<WeekDayState>> {
        /**
         * transfer the weekDays from db to listState (readOnly)
         * @param array weekDays form db
         */
        fun intArrayToReadOnlyState(array: IntArray): List<WeekDayState> {
            return (1..7).map {
                if (array.contains(it)) {
                    WeekDayState(readOnly = true, checked = true)
                } else {
                    WeekDayState(readOnly = true, checked = false)
                }
            }
        }

        /**
         * transfer the weekDays from db to listState (mutable)
         * @param array weekDays from db.
         * @param mutableList recorded mutable selections.
         */
        fun intArrayToMutableState(
            array: IntArray,
            readOnlyList: List<Int>,
            checkedList: List<Int>
        ): List<WeekDayState> {
            return (1..7).map {
                val checked = array.contains(it)
                val checkedCount = array.count()
                if (checked) {
                    WeekDayState(readOnly = checkedCount == 1, checked = checked)
                } else {
                    WeekDayState(
                        readOnlyList.contains(it) || !checkedList.contains(it),
                        checked = checked
                    )
                }
            }
        }

        val lists = mutableListOf<List<WeekDayState>>()
        var last = listOf<WeekDayState>()

        if (readOnly) {
            dailyRows.forEach { dailyRow ->
                lists.add(intArrayToReadOnlyState(dailyRow.weekdays))
            }
        } else {
            val length = dailyRows.size
            //val mutableRecordList = mutableListOf<Int>()
            val readOnlyList = mutableListOf<Int>()
            val checkedList = mutableListOf<Int>()

            var index = length - 1
            while (index >= 0) {
                val current = dailyRows[index]
                checkedList.addAll(current.weekdays.toList())
                // if the weekDay has only one option, it's will be recorded.
                if (current.weekdays.size > 1) {
                    //mutableRecordList.addAll(current.dailyRC.dailyRow.weekdays.toList())
                    // if the last has several options, it can add row from it.
//                    if (index == length - 1) {
//                        canAddRow = false
//                    }
                } else if (current.weekdays.size == 1) {
                    readOnlyList.addAll(current.weekdays.toList())
                }
                if (index == length - 1) {
                    // last is readOnly
                    lists.add(0, intArrayToReadOnlyState(current.weekdays))
                    last = (1..7).map {
                        if (current.weekdays.contains(it)) {
                            WeekDayState(readOnly = false, checked = false)
                        } else {
                            WeekDayState(readOnly = true, checked = false)
                        }
                    }

                } else {
                    lists.add(
                        0,
                        intArrayToMutableState(current.weekdays, readOnlyList, checkedList)
                    )
                }
                index--
            }
        }

        return Pair(lists, last)
    }

    fun calcIsCellValid(rowIndex: Int, cellIndex: Int): Boolean {
        val dailyRC = dailyRCs[rowIndex]
        val dailyCell = dailyRC.dailyCells[cellIndex]
        fun firstOfNormalType(normalType: Int): DailyCell {
            return dailyRC.dailyCells.first { it.normalType == normalType }
        }

        if (dailyCell.normalType < 2) {
            // 获取下一组最开始的时间
            val next = firstOfNormalType(dailyCell.normalType + 1)
            if (dailyCell.end > next.start) {
                return false
            }
        }

        val currentGroups = dailyRC.dailyCells.filter { it.normalType == dailyCell.normalType }

        // 检查是否有溢出的时间
        currentGroups.forEachIndexed { index, cell ->
            if (index > 0 && index <= dailyCell.serialIndex) {
                if (currentGroups[index - 1].end > cell.start) {
                    return false
                }
            }
        }

        return true
    }

    override fun toString(): String {
        return "DailyTableState2(dailyTRC=${dailyTRC.dailyTable.name}, currentUserUid=$currentUserUid, readOnly=$readOnly, weekDays=$weekDays, initialAddRowWeekDays=$initialAddRowWeekDays, canAddRow=$canAddRow)"
    }


    companion object {
        fun default(): DailyTableState2 = DailyTableState2(DailyTRC.default(), User.default().uid)
        fun of(dailyTRC: DailyTRC, currentUserUid: String?): DailyTableState2 =
            DailyTableState2(dailyTRC, currentUserUid)
    }
}
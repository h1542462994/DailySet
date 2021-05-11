package org.tty.dailyset.model.lifetime

import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.entity.User

/**
 * detail of [dailyTRC], use computing.
 */
class DailyTableState2(
    val dailyTRC: DailyTRC,
    val user: User,
) {
    var readOnly: Boolean = true
        internal set
    lateinit var weekDays: Iterable<Iterable<WeekDayState>>
    var canAddRow: Boolean = false
        internal set

    init {
        this.readOnly = calcReadOnly()
        this.weekDays = calcWeekDays()

    }

    private fun dailyTable(): DailyTable {
        return dailyTRC.dailyTable
    }

    private fun calcReadOnly(): Boolean {
        return dailyTable().global || dailyTable().referenceUid != user.uid
    }

    private fun calcWeekDays() : Iterable<Iterable<WeekDayState>> {
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
        fun intArrayToMutableState(array: IntArray, readOnlyList: List<Int>, checkedList: List<Int>): List<WeekDayState> {
            return (1..7).map {
                val checked = array.contains(it)
                val checkedCount = array.count()
                if (checked) {
                    WeekDayState(readOnly = checkedCount == 1, checked = checked)
                } else {
                    WeekDayState(readOnlyList.contains(it) || !checkedList.contains(it), checked = checked)
                }
            }
        }


        
        TODO("")
    }

    companion object {
        fun of(dailyTRC: DailyTRC, currentUser: User): DailyTableState2 = DailyTableState2(dailyTRC, currentUser)
    }
}
package org.tty.dailyset.bean.lifetime

import org.tty.dailyset.bean.entity.DailySetRow

/**
 * the weekDayState represents each element from [DailySetRow.weekdays]
 */
data class WeekDayState(
    val readOnly: Boolean,
    val checked: Boolean
) {
    override fun toString(): String {
        return if (checked) {
            "*"
        } else {
            " "
        }
    }
}
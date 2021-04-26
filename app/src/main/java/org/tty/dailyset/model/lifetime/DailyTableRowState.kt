package org.tty.dailyset.model.lifetime

import org.tty.dailyset.model.entity.DailyRC

class DailyTableRowState(
    val dailyRC: DailyRC,
    var weekDays: List<WeekDayState>
) {

}
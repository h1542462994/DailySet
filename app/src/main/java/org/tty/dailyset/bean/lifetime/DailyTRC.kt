package org.tty.dailyset.bean.lifetime

import org.tty.dailyset.bean.entity.DailySetCell
import org.tty.dailyset.bean.entity.DailySetRow
import org.tty.dailyset.bean.entity.DailySetTable

data class DailySetTRC(
    val dailySetTable: DailySetTable,
    val dailySetRCs: List<DailySetRC>
)

data class DailySetRC(
    val dailySetRow: DailySetRow,
    val dailySetCells: List<DailySetCell>
)
package org.tty.dailyset.bean

import org.tty.dailyset.bean.entity.DailySet

data class DailySetUpdateResult(
    val dailySet: DailySet,

    val updateItems: List<DailySetUpdateItemCollection<*>>
)
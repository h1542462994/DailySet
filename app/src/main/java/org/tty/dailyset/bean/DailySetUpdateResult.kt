package org.tty.dailyset.bean

import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.entity.DailySetResource

data class DailySetUpdateResult(
    val dailySet: DailySet,
    val updateItems: List<DailySetUpdateItemCollection<DailySetResource>>
)
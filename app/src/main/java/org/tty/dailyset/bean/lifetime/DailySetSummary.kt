package org.tty.dailyset.bean.lifetime

import org.tty.dailyset.bean.enums.DailySetIcon
import org.tty.dailyset.bean.enums.DailySetType

data class DailySetSummary(
    val uid: String,
    val type: DailySetType,
    val name: String,
    val icon: DailySetIcon?,
)
package org.tty.dailyset.event

import org.tty.dailyset.bean.entity.DailySetIcon
import org.tty.dailyset.bean.entity.DailySetType

@Deprecated("not used yet.", level = DeprecationLevel.WARNING)
data class DailySetCreateEventArgs(
    val dailySetName: String,
    val uid: String,
    val ownerUid: String,
    val type: DailySetType,
    val icon: DailySetIcon?,
): EventArgs
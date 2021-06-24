package org.tty.dailyset.event

import org.tty.dailyset.model.entity.DailySetIcon
import org.tty.dailyset.model.entity.DailySetType

data class DailySetCreateEventArgs(
    val dailySetName: String,
    val uid: String,
    val ownerUid: String,
    val type: DailySetType,
    val icon: DailySetIcon?,
): EventArgs
package org.tty.dailyset.event
import org.tty.dailyset.bean.entity.DailyTRC

data class DailyTableCreateEventArgs(
    val name: String,
    val cloneFrom: DailyTRC,
    val uid: String,
    val referenceUid: String
): EventArgs

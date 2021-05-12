package org.tty.dailyset.event
import org.tty.dailyset.model.entity.DailyTRC

data class DailyTableCreateEventArgs(
    val name: String,
    val cloneFrom: DailyTRC,
    val uid: String,
    val referenceUid: String
): EventArgs

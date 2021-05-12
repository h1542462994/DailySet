package org.tty.dailyset.event
import org.tty.dailyset.model.entity.DailyTable

data class DailyTableCreateEventArgs(
    val name: String,
    val cloneFrom: DailyTable,
    val uid: String,
    val referenceUid: String
): EventArgs

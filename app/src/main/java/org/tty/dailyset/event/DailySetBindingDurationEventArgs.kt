package org.tty.dailyset.event

@Deprecated("not used yet.", level = DeprecationLevel.WARNING)
data class DailySetBindingDurationEventArgs(
    val dailySetUid: String,
    val dailyDurationUid: String,
    val bindingDailyTableUid: String
): EventArgs
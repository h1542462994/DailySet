package org.tty.dailyset.event

data class DailySetBindingDurationEventArgs(
    val dailySetUid: String,
    val dailyDurationUid: String,
    val bindingDailyTableUid: String
): EventArgs
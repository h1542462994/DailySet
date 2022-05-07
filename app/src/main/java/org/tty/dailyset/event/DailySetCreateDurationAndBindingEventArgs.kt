package org.tty.dailyset.event

import org.tty.dailyset.bean.entity.PeriodCode
import java.time.LocalDate

@Deprecated("not used yet.", level = DeprecationLevel.WARNING)
data class DailySetCreateDurationAndBindingEventArgs(
    val dailyDurationUid: String,
    val dailySetUid: String,
    val name: String,
    val ownerUid: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val periodCode: PeriodCode,
    val bindingDailyTableUid: String
): EventArgs
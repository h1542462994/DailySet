package org.tty.dailyset.data.processor

import org.tty.dailyset.model.entity.PeriodCode
import java.time.LocalDate

interface ClazzDailySetProcessor {
    fun onCreateClazzDurationAndBinding(name: String, uid: String?, startDate: LocalDate, endDate: LocalDate, periodCode: PeriodCode, bindingDailyTableUid: String)
    fun onBindingClazzDuration(dailySetUid: String, dailyDurationUid: String, bindingDailyTableUid: String)
}
package org.tty.dailyset.data.processor

import org.tty.dailyset.model.entity.DailySetIcon
import org.tty.dailyset.model.entity.DailySetType
import org.tty.dailyset.model.entity.PeriodCode
import java.time.LocalDate

interface DailySetProcessor {
    fun onCreate(dailySetName: String, icon: DailySetIcon?, type: DailySetType)

}
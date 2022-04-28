package org.tty.dailyset.datasource.runtime

import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

interface RuntimeDataSource {
    val now: Flow<LocalDateTime>
    val nowDate: Flow<LocalDate>
    val nowDayOfWeek: Flow<DayOfWeek>

    fun init()
}
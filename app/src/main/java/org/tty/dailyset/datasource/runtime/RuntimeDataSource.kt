package org.tty.dailyset.datasource.runtime

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.tty.dailyset.ui.page.MainPageTabs
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

interface RuntimeDataSource {
    val now: Flow<LocalDateTime>
    val nowDate: Flow<LocalDate>
    val nowDayOfWeek: Flow<DayOfWeek>
    val mainTab: MutableSharedFlow<MainPageTabs>
    val currentDailySetUid: MutableSharedFlow<String>

    fun init()

}
package org.tty.dailyset.datasource.runtime

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.tty.dailyset.component.common.*
import org.tty.dailyset.ui.page.MainPageTabs
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class RuntimeDataSourceImpl(private val sharedComponents: SharedComponents) : RuntimeDataSource {

    /**
     * 时钟源
     */
    override val now = flow {
        while (true) {
            emit(LocalDateTime.now())
            delay(200)
        }
    }.asActivityColdStateFlow(LocalDateTime.now())

    override val nowDate: Flow<LocalDate> = now.map { it.toLocalDate() }.distinctUntilChanged()

    override val nowDayOfWeek: Flow<DayOfWeek> = nowDate.map { it.dayOfWeek }.distinctUntilChanged()

    override val mainTab = MutableStateFlow(MainPageTabs.SUMMARY)

    override val currentDailySetUid: MutableStateFlow<String> = MutableStateFlow("")

}
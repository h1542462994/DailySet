package org.tty.dailyset.component.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.entity.DailyTable
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.datasource.db.PreferenceDao
import org.tty.dailyset.ui.page.MainPageTabs
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

interface StateStore {
    /**
     * **seedVersion** 数据库种子版本号
     * @see PreferenceDao
     */
    val seedVersion: Flow<Int>

    /**
     * **currentUserUid** 当前用户uid
     */
    val currentUserUid: Flow<String>

    val currentUserUidSnapshot: String

    /**
     * **now** 当前时间
     */
    val now: Flow<LocalDateTime>

    /**
     * **nowDate** 当前日期
     */
    val nowDate: Flow<LocalDate>

    /**
     * **nowDayOfWeek** 当前星期
     */
    val nowDayOfWeek: Flow<DayOfWeek>

    /**
     * **startDayOfWeek** 开始星期
     */
    val startDayOfWeek: Flow<DayOfWeek>

    val mainTab: MutableSharedFlow<MainPageTabs>
    val users: Flow<List<User>>
    val currentUser: Flow<User>
    val dailyTables: Flow<List<DailyTable>>
    val dailySets: Flow<List<DailySet>>
    val currentDailySetUid: MutableSharedFlow<String>
}
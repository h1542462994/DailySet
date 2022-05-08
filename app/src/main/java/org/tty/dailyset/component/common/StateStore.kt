package org.tty.dailyset.component.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.*
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
    val currentUserUid: StateFlow<String>

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

    /**
     * **mainTab** 主页tab
     */
    val mainTab: MutableStateFlow<MainPageTabs>


    val users: Flow<List<User>>

    /**
     * **currentUser** 当前用户
     */
    val currentUser: Flow<User>
    val userTicketInfo: Flow<UserTicketInfo>
    val dailySetTables: Flow<List<DailySetTable>>

    /**
     * **dailySets** 当前的日程
     */
    val dailySets: Flow<List<DailySet>>

    /**
     * **currentDailySetUid** 当前显示日程的uid
     */
    val currentDailySetUid: MutableStateFlow<String>

    /**
     * **firstLoadUser** 是否需要跳转到登陆页面
     */
    val firstLoadUser: Flow<Boolean>

    /**
     * 当前*http*服务的地址
     */
    val currentHttpServerAddress: Flow<String>

    /**
     * **deviceCode** 设备码，默认为空，只要该设备登录成功过一次，就会赋予一个唯一的设备码
     */
    val deviceCode: StateFlow<String>

    /**
     * 当前*grpc*服务的地址
     */
    val currentHost: Flow<String>

    /**
     * **dailySetVisibles** 对当前用户可见的日程集
     */
    val dailySetVisibles: Flow<List<DailySetVisible>>
}
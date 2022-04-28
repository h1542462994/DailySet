package org.tty.dailyset.component.common

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.datasource.db.PreferenceDao
import java.time.DayOfWeek
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

    /**
     * **now** 当前时间
     */
    val now: Flow<LocalDateTime>

    /**
     * **nowDayOfWeek** 当前星期
     */
    val nowDayOfWeek: Flow<DayOfWeek>

    /**
     * **startDayOfWeek** 开始星期
     */
    val startDayOfWeek: Flow<DayOfWeek>
}
package org.tty.dailyset.data.scope

import androidx.compose.runtime.Immutable

@Immutable
interface DataScope: WeekDaySelectStateScope, DailyTableScope, UserScope {
    companion object: DataScope
}

package org.tty.dailyset.data.scope

import androidx.compose.runtime.Immutable

/**
 * root of data operations,
 * see also [DailyTableScope], [UserScope]
 * usage: with(DataScope) { ... }
 */
@Immutable
interface DataScope: DailyTableScope, UserScope {
    companion object: DataScope
}

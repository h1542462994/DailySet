package org.tty.dailyset.bean.lifetime

import androidx.compose.runtime.Immutable
import org.tty.dailyset.bean.enums.DailySetIcon
import org.tty.dailyset.bean.enums.DailySetType

/**
 * dailySetSummary, which is **immutable**.
 */
@Immutable
data class DailySetSummary(
    val uid: String,
    val type: DailySetType,
    val name: String,
    val icon: DailySetIcon?,
)
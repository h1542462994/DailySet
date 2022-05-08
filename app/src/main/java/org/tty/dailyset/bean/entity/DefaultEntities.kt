package org.tty.dailyset.bean.entity

import org.tty.dailyset.bean.enums.DailySetType
import org.tty.dailyset.bean.lifetime.DailySetSummary

object DefaultEntities {
    fun emptyDailySet(): DailySet {
        return DailySet(
            uid = "#local",
            type = DailySetType.Normal.value,
            sourceVersion = 0,
            matteVersion = 0,
            metaVersion = 0
        )
    }

    fun emptyDailySetSummary(): DailySetSummary {
        return DailySetSummary(
            uid = "",
            type = DailySetType.Normal,
            name = "",
            icon = null
        )
    }
}
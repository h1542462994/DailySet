package org.tty.dailyset.bean.entity

import org.tty.dailyset.bean.enums.DailySetType

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
}
package org.tty.dailyset.bean.entity

import org.tty.dailyset.bean.enums.DailySetPeriodCode
import org.tty.dailyset.bean.enums.DailySetType
import org.tty.dailyset.bean.lifetime.DailySetClazzAutoPageInfo
import org.tty.dailyset.bean.lifetime.DailySetSummary
import org.tty.dailyset.bean.lifetime.DailySetTRC
import java.time.LocalDate

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

    fun emptyDailySetTable(): DailySetTable {
        return DailySetTable(
            sourceUid = "",
            name = ""
        )
    }

    fun emptyDailySetTRC(): DailySetTRC {
        return DailySetTRC(
            dailySetTable = emptyDailySetTable(),
            dailySetRCs = emptyList()
        )
    }

    fun emptyDailySetClazzAutoPageInfo(): DailySetClazzAutoPageInfo {
        return DailySetClazzAutoPageInfo(
            2018,
            DailySetPeriodCode.FirstTerm,
            1,
            LocalDate.of(2018,9,1),
            LocalDate.of(2018,12,31)
        )
    }
}
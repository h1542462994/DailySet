package org.tty.dailyset.database.processor

import org.tty.dailyset.bean.entity.DailySetIcon
import org.tty.dailyset.bean.entity.DailySetType

interface DailySetProcessor {
    fun onCreate(dailySetName: String, icon: DailySetIcon?, type: DailySetType)

}
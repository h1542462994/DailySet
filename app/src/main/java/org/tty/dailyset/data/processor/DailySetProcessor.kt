package org.tty.dailyset.data.processor

import org.tty.dailyset.model.entity.DailySetIcon
import org.tty.dailyset.model.entity.DailySetType

interface DailySetProcessor {
    fun onCreate(dailySetName: String, icon: DailySetIcon?, type: DailySetType)
}
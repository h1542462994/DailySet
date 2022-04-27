package org.tty.dailyset.datasource

import org.tty.dailyset.datasource.db.*

interface DbSourceCollection {
    val userDao: UserDao
    val preferenceDao: PreferenceDao
    val dailySetDao: DailySetDao
    val dailyTableDao: DailyTableDao
    val dailyRowDao: DailyRowDao
    val dailyCellDao: DailyCellDao
    val dailyDurationDao: DailyDurationDao
    val dailySetBindingDao: DailySetBindingDao
}
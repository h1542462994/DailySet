package org.tty.dailyset.repository

interface RepositoryCollection {
    val userRepository: UserRepository
    val preferenceRepository: PreferenceRepository
    val dailyTableRepository: DailyTableRepository
    val dailySetRepository: DailySetRepository
}
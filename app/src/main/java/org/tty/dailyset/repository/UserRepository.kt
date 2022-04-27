package org.tty.dailyset.repository

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.datasource.db.UserDao
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.component.common.SharedComponents

/**
 * repository for [User]
 * it is used in [org.tty.dailyset.DailySetApplication],
 * it will use db service, see also [org.tty.dailyset.database.DailySetRoomDatabase]
 */
class UserRepository(private val sharedComponents: SharedComponents) {
    private val userDao: UserDao get() = sharedComponents.dataSourceCollection.dbSourceCollection.userDao

    val users: Flow<List<User>> = userDao.all()
    fun load(uid: String): Flow<User?> = userDao.load(uid)
}
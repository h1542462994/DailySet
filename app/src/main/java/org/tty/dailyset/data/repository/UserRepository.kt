package org.tty.dailyset.data.repository

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.dao.UserDao
import org.tty.dailyset.model.entity.User

class UserRepository(private val userDao: UserDao) {
    val users: Flow<List<User>> = userDao.all()
    fun load(uid: String): Flow<User?> = userDao.load(uid)
}
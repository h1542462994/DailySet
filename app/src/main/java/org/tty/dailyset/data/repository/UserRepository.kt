package org.tty.dailyset.data.repository

import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.tty.dailyset.model.dao.UserDao
import org.tty.dailyset.model.entity.User

class UserRepository(private val userDao: UserDao) {
    val users: Flow<List<User>> = userDao.all()
    fun load(uid: String): Flow<User?> = userDao.load(uid)
}
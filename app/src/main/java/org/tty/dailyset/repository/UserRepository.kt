package org.tty.dailyset.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.tty.dailyset.MainDestination
import org.tty.dailyset.datasource.db.UserDao
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.enums.IndexLoadResult
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.login.LoginInput

/**
 * repository for [User]
 * it is used in [org.tty.dailyset.DailySetApplication],
 * it will use db service, see also [org.tty.dailyset.database.DailySetRoomDatabase]
 */
class UserRepository(private val sharedComponents: SharedComponents) {
    private val userDao: UserDao get() = sharedComponents.dataSourceCollection.dbSourceCollection.userDao

    val users: Flow<List<User>> = userDao.all()
    fun load(uid: String): Flow<User?> = userDao.load(uid)

    suspend fun firstLoad(): IndexLoadResult {
        val firstLoad = sharedComponents.stateStore.firstLoadUserSnapshot
        delay(1000)
        return if (firstLoad) {
            withContext(Dispatchers.Main) {
                //sharedComponents.nav.action.toLogin(LoginInput(MainDestination.INDEX))
                sharedComponents.nav.action.toMain()
            }
            IndexLoadResult.NotLoad
        } else {
            IndexLoadResult.AutoLoginFail
        }
    }
}
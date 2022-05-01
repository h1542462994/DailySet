package org.tty.dailyset.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tty.dailyset.MainDestination
import org.tty.dailyset.bean.ResponseCodes
import org.tty.dailyset.bean.ResponseCodes.success
import org.tty.dailyset.datasource.db.UserDao
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.enums.PlatformCode
import org.tty.dailyset.bean.enums.PlatformState
import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.bean.req.UserLoginReq
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.component.common.SuspendInit
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.showToast
import org.tty.dailyset.component.common.showToastOfNetworkError
import org.tty.dailyset.component.login.LoginInput

/**
 * repository for [User]
 * it is used in [org.tty.dailyset.DailySetApplication],
 * it will use db service, see also [org.tty.dailyset.database.DailySetRoomDatabase]
 */
class UserRepository(private val sharedComponents: SharedComponents): SuspendInit {
    private val userDao: UserDao get() = sharedComponents.dataSourceCollection.dbSourceCollection.userDao

    val users: Flow<List<User>> = userDao.all()
    fun load(uid: String): Flow<User?> = userDao.load(uid)

    override suspend fun init() {
        delay(500)
        if (sharedComponents.repositoryCollection.preferenceRepository.read(PreferenceName.FIRST_LOAD_USER) { it.toBooleanStrict() }) {
            withContext(Dispatchers.Main) {
                sharedComponents.nav.action.toLogin(LoginInput(MainDestination.INDEX))
                //sharedComponents.nav.action.toMain()
            }
        } else {
            withContext(Dispatchers.Main) {
                sharedComponents.nav.action.toMain()
            }
        }
    }

    suspend fun login(uid: String, password: String) {
        try {
            val userService = sharedComponents.dataSourceCollection.netSourceCollection.userService
            val deviceName: String = android.os.Build.DEVICE
            var deviceCode: String? = sharedComponents.stateStore.deviceCode.value
            if (deviceCode == "") deviceCode = null
            val platformCode = PlatformCode.PORTABLE.code
            val userLoginReq = UserLoginReq(uid.toInt(), password, deviceCode, deviceName, platformCode)

            val userLoginResp = userService.login(userLoginReq)
            if (userLoginResp.code == ResponseCodes.success) {
                // 登录成功的逻辑
                checkNotNull(userLoginResp.data) { "userLoginResp.data is null" }

                // 更新数据
                sharedComponents.repositoryCollection.preferenceRepository.save(PreferenceName.FIRST_LOAD_USER, false)
                sharedComponents.repositoryCollection.preferenceRepository.save(PreferenceName.DEVICE_CODE, userLoginResp.data.deviceCode)
                sharedComponents.repositoryCollection.preferenceRepository.save(PreferenceName.CURRENT_USER_UID, userLoginResp.data.uid.toString())
                sharedComponents.dataSourceCollection.dbSourceCollection.userDao.update(User(
                    userUid = userLoginResp.data.uid.toString(),
                    name = userLoginResp.data.uid.toString(),
                    nickName = userLoginResp.data.nickname,
                    token = userLoginResp.data.token,
                    localUser = false,
                    state = PlatformState.ALIVE.state
                ))
                withContext(Dispatchers.Main) {
                    // 跳转到主页
                    sharedComponents.nav.action.toMain()
                }
            } else {
                showToast("(${userLoginResp.code}) ${userLoginResp.message}")
            }

        } catch (e: Exception) {
            logger.e("UserRepository", "${e.javaClass} :: ${e.message}")
            showToastOfNetworkError("登录失败", e)
        }

    }
}
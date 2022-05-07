package org.tty.dailyset.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.tty.dailyset.MainActions
import org.tty.dailyset.MainDestination
import org.tty.dailyset.bean.ResponseCodes
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.entity.UserTicketInfo
import org.tty.dailyset.bean.enums.PlatformCode
import org.tty.dailyset.bean.enums.PlatformState
import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.bean.enums.UnicTickStatus
import org.tty.dailyset.bean.req.UserAutoLoginReq
import org.tty.dailyset.bean.req.UserLoginReq
import org.tty.dailyset.bean.req.UserRegisterReq
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.component.common.*
import org.tty.dailyset.component.login.LoginInput
import org.tty.dailyset.converter.toUnicTicketStatus
import org.tty.dailyset.dailyset_cloud.grpc.Token
import org.tty.dailyset.datasource.db.UserDao

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
                sharedComponents.nav.action.toLogin(LoginInput(MainDestination.INDEX, ""))
                //sharedComponents.nav.action.toMain()
            }
        } else {
            withContext(Dispatchers.Main) {
                sharedComponents.nav.action.toMain()
            }
            collectData()
        }
    }

    /**
     * 首次启动时，需要从服务端获取用户信息。
     */
    private suspend fun collectData() {
        autoLogin()
        getCurrentBindTicketInfo()
    }

    suspend fun login(uid: String, password: String, navAction: MainActions) {
        try {
            val userService = sharedComponents.dataSourceCollection.netSourceCollection.userService
            val deviceName: String = getDeviceName()
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
                    navAction.toMain()

                    // TODO: 启动后续的自动同步逻辑...
                }
            } else {
                showToastAsync("(${userLoginResp.code}) ${userLoginResp.message}")
            }

        } catch (e: Exception) {
            logger.e("UserRepository", "${e.javaClass.simpleName} :: ${e.message}")
            // TODO: 本地化处理
            showToastOfNetworkError("登录失败", e)
        }
    }

    private suspend fun autoLogin() {
        val userService = sharedComponents.dataSourceCollection.netSourceCollection.userService
        val currentUser = getCurrentUser() ?: return
        try {
            logger.d("UserRepository", "currentToken: $currentUser")

            val userAutoLoginReq = UserAutoLoginReq(token = currentUser.token)
            val userAutoLoginResp = userService.autoLogin(userAutoLoginReq)

            if (userAutoLoginResp.code == ResponseCodes.success) {
                // 登录成功的逻辑
                checkNotNull(userAutoLoginResp.data) { "userLoginResp.data is null" }

                // 更新数据
                sharedComponents.repositoryCollection.preferenceRepository.save(PreferenceName.FIRST_LOAD_USER, false)
                sharedComponents.repositoryCollection.preferenceRepository.save(PreferenceName.DEVICE_CODE, userAutoLoginResp.data.deviceCode)
                sharedComponents.repositoryCollection.preferenceRepository.save(PreferenceName.CURRENT_USER_UID, userAutoLoginResp.data.uid.toString())
                sharedComponents.dataSourceCollection.dbSourceCollection.userDao.update(User(
                    userUid = userAutoLoginResp.data.uid.toString(),
                    name = userAutoLoginResp.data.uid.toString(),
                    nickName = userAutoLoginResp.data.nickname,
                    token = userAutoLoginResp.data.token,
                    localUser = false,
                    state = PlatformState.ALIVE.state
                ))

                showToastAsync("自动登录成功")
            } else {
                showToastAsync("(${userAutoLoginResp.code}) ${userAutoLoginResp.message}")
                sharedComponents.dataSourceCollection.dbSourceCollection.userDao.update(
                    currentUser.copy(
                        state = PlatformState.LEAVE.state
                    )
                )
            }

        } catch (e: Exception) {
            logger.e("UserRepository", "autoLogin, ${e.javaClass.simpleName} :: ${e.message}")
            sharedComponents.dataSourceCollection.dbSourceCollection.userDao.update(
                currentUser.copy(
                    state = PlatformState.LEAVE.state
                )
            )
            // TODO: 本地化处理
            showToastOfNetworkError("自动登录失败", e)
        }
    }

    suspend fun register(nickname: String, email: String, password: String, navAction: MainActions) {
        try {
            val userRegisterReq = UserRegisterReq(nickname, email, password)
            val userService = sharedComponents.dataSourceCollection.netSourceCollection.userService
            val response = userService.register(userRegisterReq)

            // 注册成功的逻辑
            if (response.code == ResponseCodes.success) {
                checkNotNull(response.data)  { "userRegisterResp.data is null" }
                withContext(Dispatchers.Main) {
                    navAction.toLogin(LoginInput(MainDestination.REGISTER, response.data.uid.toString()))
                }
            } else {
                showToastAsync("(${response.code}) ${response.message}")
            }


        } catch (e: Exception) {
            logger.e("UserRepository", "${e.javaClass.simpleName} :: ${e.message}")
            // TODO: 本地化处理
            showToastOfNetworkError("注册失败", e)
        }
    }

    suspend fun testHello() {
        try {
            sharedComponents.repositoryCollection.preferenceRepository.save(PreferenceName.CURRENT_HOST, "192.168.31.10")

            val helloCoroutineStub = sharedComponents.dataSourceCollection.grpcSourceCollection.helloService()
            val response = helloCoroutineStub.sayHello {
                name = "你好啊!"
            }
            showToastAsync(response.message)
        } catch (e: Exception) {
            logger.e("UserRepository", "${e.javaClass.simpleName} :: ${e.message}")
            showToastOfNetworkError("欢迎失败", e)
        }

    }

    private suspend fun getCurrentBindTicketInfo() {
        val user = getCurrentUser() ?: return
        val userTicketInfoDao = sharedComponents.dataSourceCollection.dbSourceCollection.userTicketInfoDao
        val ticketService = sharedComponents.dataSourceCollection.grpcSourceCollection.ticketService()
        try {
            val emptyUserTicketInfo = UserTicketInfo(
                user.userUid,
                status = UnicTickStatus.NotBind,
                studentUid = "",
                departmentName = "",
                clazzName = "",
                name = "",
                grade = 0
            )

            val response = ticketService.currentBindInfo {
                this.token = Token.newBuilder().setValue(user.token).build()
            }
            if (response.code == ResponseCodes.ticketNotExist) {
                // 没有ticket的绑定信息
                userTicketInfoDao.update(emptyUserTicketInfo)
            } else {
                val ticketInfo = emptyUserTicketInfo.copy(
                    status = response.bindInfo.status.toUnicTicketStatus(),
                    studentUid = response.bindInfo.uid,
                    departmentName = response.bindInfo.departmentName,
                    clazzName = response.bindInfo.className,
                    name = response.bindInfo.name,
                    grade = response.bindInfo.grade
                )
                userTicketInfoDao.update(ticketInfo)
            }
            showToastAsync("获取绑定信息成功")
        } catch (e: Exception) {
            logger.e("UserRepository", "getCurrentBindTicketInfo, ${e.javaClass.simpleName} :: ${e.message}")
            e.printStackTrace()
            showToastOfNetworkError("获取绑定信息失败", e)
        }
    }

    suspend fun bindTicket(studentUid: String, password: String, navAction: MainActions) {
        val user = getCurrentUser() ?: return
        val ticketService = sharedComponents.dataSourceCollection.grpcSourceCollection.ticketService()
        try {
            val response = ticketService.bind {
                this.token = Token.newBuilder().setValue(user.token).build()
                this.uid = studentUid
                this.password = password
            }
            if (response.code == ResponseCodes.success) {
                showToastAsync(response.message)
                postBindTicket(navAction)
            } else {
                showToastAsync("(${response.code}) ${response.message}")
            }
        } catch (e: Exception) {
            logger.e("UserRepository", "bindTicket, ${e.javaClass.simpleName} :: ${e.message}")
            showToastOfNetworkError("绑定失败", e)
        }
    }

    private suspend fun postBindTicket(navAction: MainActions) {
        // 退出绑定界面。
        navAction.upPress()
        getCurrentBindTicketInfo()
    }

    private suspend fun getCurrentUser(): User? {
        val currentUserUid: String = sharedComponents.repositoryCollection.preferenceRepository.read(PreferenceName.CURRENT_USER_UID)
        if (currentUserUid.startsWith("#")) {
            return null
        }
        return sharedComponents.dataSourceCollection.dbSourceCollection.userDao.get(currentUserUid)
    }

}
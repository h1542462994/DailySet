package org.tty.dailyset.component.common

import kotlinx.coroutines.flow.*
import org.tty.dailyset.bean.entity.*
import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.common.observable.flow2
import org.tty.dailyset.ui.page.MainPageTabs
import org.tty.dioc.util.optional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@Suppress("OPT_IN_USAGE")
class StateStoreImpl(private val sharedComponents: SharedComponents): StateStore {

    override val seedVersion: Flow<Int> get() = loadPreference(PreferenceName.SEED_VERSION, mapper = { it.toInt() })
    override val currentUserUid: StateFlow<String> = loadPreferenceAsStateFlow(PreferenceName.CURRENT_USER_UID)
    override val firstLoadUser: Flow<Boolean> = loadPreference(PreferenceName.FIRST_LOAD_USER, mapper = { it.toBooleanStrict() })

    override val now: Flow<LocalDateTime> = sharedComponents.dataSourceCollection.runtimeDataSource.now
    override val nowDate: Flow<LocalDate> = sharedComponents.dataSourceCollection.runtimeDataSource.nowDate
    override val nowDayOfWeek: Flow<DayOfWeek> = sharedComponents.dataSourceCollection.runtimeDataSource.nowDayOfWeek
    override val startDayOfWeek: Flow<DayOfWeek> = loadPreference(PreferenceName.START_DAY_OF_WEEK, mapper = { DayOfWeek.of(it.toInt()) })
    override val mainTab: MutableStateFlow<MainPageTabs> = sharedComponents.dataSourceCollection.runtimeDataSource.mainTab

    override val users: Flow<List<User>> = sharedComponents.database.userDao().all()
    override val currentUser = flow2(currentUserUid, users) { uid, users ->
        users.find { it.userUid == uid } ?: DefaultEntities.emptyUser()
    }

    override val userTicketInfo: Flow<UserTicketInfo> = currentUserUid.flatMapLatest { userUid ->
        sharedComponents.database.userTicketInfoDao().load(userUid).map {
            it ?: DefaultEntities.emptyUserTicketInfo()
        }
    }

    override val dailySetTables: Flow<List<DailySetTable>> = sharedComponents.database.dailySetTableDao().all()
    override val dailySets: Flow<List<DailySet>> = sharedComponents.database.dailySetDao().allFlow()
    override val currentDailySetUid: MutableStateFlow<String> = sharedComponents.dataSourceCollection.runtimeDataSource.currentDailySetUid
    override val currentHttpServerAddress: Flow<String> = loadPreference(PreferenceName.CURRENT_HTTP_SERVER_ADDRESS)
    override val deviceCode: StateFlow<String> = loadPreferenceAsStateFlow(PreferenceName.DEVICE_CODE)
    override val currentHost: Flow<String> = loadPreference(PreferenceName.CURRENT_HOST)
    override val dailySetVisibles: Flow<List<DailySetVisible>> = currentUserUid.flatMapLatest {
        sharedComponents.database.dailySetVisibleDao().allByUserUidFlow(it)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> loadPreference(preferenceName: PreferenceName, mapper: (it: String) -> T = { it as T }): Flow<T> {
        return sharedComponents.database.preferenceDao().load(preferenceName.key).map {
            it.optional { mapper(value) } ?: mapper(preferenceName.defaultValue)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> loadPreferenceAsStateFlow(preferenceName: PreferenceName, mapper: (it: String) -> T = { it as T }): StateFlow<T> {
        return loadPreference(preferenceName, mapper).asActivityHotStateFlow(mapper(preferenceName.defaultValue))
    }
}
package org.tty.dailyset.viewmodel

import androidx.lifecycle.ViewModel
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.common.local.ComponentViewModel
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.common.observable.*
import org.tty.dailyset.model.entity.DailySetDurations
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.entity.PreferenceName
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.model.lifetime.UserState
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetCursors
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableState2
import org.tty.dailyset.ui.page.MainPageTabs
import org.tty.dioc.util.pair
import java.time.LocalDate

class MainViewModel(val service: DailySetApplication): ViewModel() {

    init {
        // register the viewModel
        ComponentViewModel provides this
    }

    override fun onCleared() {
        // unregister the viewModel
        ComponentViewModel.pop()
    }

    /**
     * init the viewModel
     */
    fun init() {
    }

    //region liveData Internal
    //---main
    private val mainTabLiveData = liveData(MainPageTabs.DAILY_SET)
    private val currentDailyTableUidLiveData = liveData(DailyTable.default)

    //---user,preference
    private val seedVersionLiveData = liveData(service.preferenceRepository.seedVersion)
    private val currentUserUidLiveData = liveData(service.preferenceRepository.currentUserUid)
    private val usersLiveData = liveData(service.userRepository.users)
    private val currentUserStateLiveData = liveData2Map(usersLiveData, currentUserUidLiveData, "currentUser") { users, currentUserUid ->
        val currentUser = users.find { it.userUid == currentUserUid } ?: User.default()
        UserState(currentUser, currentUserUid)
    }
    //---dailyTable
    private val dailyTableSummariesLiveData = liveData(service.dailyTableRepository.dailyTableSummaries)
    private val currentDailyTRCLiveData = liveDataMapAsync(currentDailyTableUidLiveData, "currentDailyTRC") {
        service.dailyTableRepository.loadDailyTRC(it)
    }.ignoreNull()
    private val currentDailyTableState2LiveData = liveData2Map(currentDailyTRCLiveData, currentUserUidLiveData) { value1, value2 ->
        DailyTableState2(value1, value2)
    }
    // ---dailySets
    private val dailySetsLiveData = liveData(service.dailySetRepository.dailySets)
    private val normalDailyDurationsLiveData = liveData(service.dailySetRepository.normalDailyDurations)
    private val clazzDailyDurationsLiveData = liveData(service.dailySetRepository.clazzDailyDurations)

    /**
     * {mutable, runtimeOnly} the cache for clazzDailySetCursorCache
     */
    private val clazzDailySetCursorCache = HashMap<String, Int>()
    private val currentDailySetUidLiveData = liveData("")
    private val currentDailySetUidLiveDataEffect = currentDailySetUidLiveData.preEffect {
        logger.d(TAG, "dailySetUid -> $it")
        currentCursorIndexLiveData.postValue(null)
    }
    private val currentDailySetDurationsLiveData = liveDataMapAsync(currentDailySetUidLiveDataEffect, "DailySetDurations") {
        service.dailySetRepository.loadDailySetDurations(it)
    }.ignoreNull()
    private val currentCursorsLiveData = liveDataMap(currentDailySetDurationsLiveData, "DailySetCursors") {
        ClazzDailySetCursors(it)
    }


    /**
     * {mutable}
     */
    val currentCursorIndexLiveData = liveData<Int?>(null)
    private val realCursorsAndIndexLiveData = liveData2Map(currentCursorIndexLiveData, currentCursorsLiveData, "realCursorsAndIndexLiveData") { value1, value2 ->
        // save the cursor to the cache.
        var needReload = false
        val cacheKey = value2.dailySetDurations.dailySet.uid
        if (value1 == null && (clazzDailySetCursorCache[cacheKey] == null
            || clazzDailySetCursorCache[cacheKey] !in value2.list.indices)
        ) {
            needReload = true
        }
        if (value1 != null && value1 !in value2.list.indices) {
            needReload = true
        }

        if (needReload) {
            val index = value2.findIndex(LocalDate.now())
            clazzDailySetCursorCache[cacheKey] = index
            return@liveData2Map pair(value2, index)
        } else {
            return@liveData2Map pair(value2, clazzDailySetCursorCache[cacheKey]!!)
        }
    }
    private val currentDailySetBindingLiveData = liveDataMapAsync(realCursorsAndIndexLiveData) {
        val cursor = it.first.list[it.second]
        service.dailySetRepository.loadDailySetBinding(it.first.dailySetDurations.dailySet.uid, cursor.dailyDurationUid)
    }.ignoreNull()
    private val dailyTableTRCBindingLiveData = liveDataMapAsync(currentDailySetBindingLiveData, "dailyTableTRCBinding") {
        service.dailyTableRepository.loadDailyTRC(it.bindingDailyTableUid)
    }.ignoreNull()
    private val dailyTableState2BindingLiveData = liveData2Map(dailyTableTRCBindingLiveData, currentUserUidLiveData, "dailyTableState2Binding") { value1, value2 ->
        DailyTableState2(value1, value2)
    }
    private var currentClazzDailySetStateLiveData = liveData2Map(realCursorsAndIndexLiveData, dailyTableState2BindingLiveData, "ClazzDailySetState") { value1, value2 ->
        ClazzDailySetState(value1.first, value2, value1.second, this)
    }


    //endregion


    //region dailyTable.settings scope

    //region dailySet scope





    //endregion

    //region liveData Exports
    /**
     * {Mutable} MainPage中tab页的设置
     */
    val mainTab = mainTabLiveData.initial(MainPageTabs.DAILY_SET)

    /**
     * 数据库的版本号
     */
    val seedVersion = seedVersionLiveData.initial(PreferenceName.SEED_VERSION.defaultValue.toInt())

    /**
     * 当前用户的Uid
     */
    val currentUserUid = currentUserUidLiveData.initial(PreferenceName.CURRENT_USER_UID.defaultValue)

    /**
     * 数据库中记录的用户
     */
    val users = usersLiveData.initial(listOf())

    /**
     * 当前用户状态
     */
    val currentUserState = currentUserStateLiveData.initial(UserState(User.default(), User.local))

    /**
     * 所有DailyTable
     */
    val dailyTableSummaries = dailyTableSummariesLiveData.initial(listOf())

    /**
     * {Mutable} 当前DailyTable的Uid
     */
    val currentDailyTableUid = currentDailyTableUidLiveData.initial(DailyTable.default)

    /**
     * 当前DailyTable的状态集
     */
    val currentDailyTableState2 = currentDailyTableState2LiveData.initial(DailyTableState2.default())

    /**
     * 所有DailySet
     */
    val dailySets = dailySetsLiveData.initial(listOf())

    /**
     * 所有normal DailyDuration
     */
    val normalDailyDurations = normalDailyDurationsLiveData.initial(listOf())

    /**
     * 所有clazz DailyDuration
     */
    val clazzDailyDurations = clazzDailyDurationsLiveData.initial(listOf())

    /**
     * 当前dailySetUid
     */
    val currentDailySetUid = currentDailySetUidLiveData.initial("")

    /**
     * 当前DailySetDurations
     */
    val currentDailySetDurations = currentDailySetDurationsLiveData.initial(DailySetDurations.empty())

    val currentClazzDailySetState = currentClazzDailySetStateLiveData.initial(ClazzDailySetState.empty())
    //endregion


    companion object {
        const val TAG = "MainViewModel"
    }


}
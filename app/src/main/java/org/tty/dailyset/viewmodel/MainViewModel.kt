package org.tty.dailyset.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.common.local.ComponentViewModel
import org.tty.dailyset.common.local.Tags
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.common.observable.*
import org.tty.dailyset.model.entity.*
import org.tty.dailyset.model.lifetime.PagerInfo
import org.tty.dailyset.model.lifetime.UserState
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetCursors
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableState2
import org.tty.dailyset.ui.page.MainPageTabs
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class MainViewModel(val service: DailySetApplication) : ViewModel() {

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

    //region mainPart 主要部分的LiveData

    /**
     * 时钟源
     */
    private val nowFlow = flow<LocalDateTime> {
        emit(LocalDateTime.now())
    }

    /**
     * 今天的日期
     */
    private val nowDateFlow = nowFlow.map { it.toLocalDate() }.distinctUntilChanged().onEach {
        // side effect.
        // update the clazzWeekDay.
        clazzWeekDayLiveData.postValue(it.dayOfWeek)
    }

    /**
     * mutable | 一周的开始星期，限制为[DayOfWeek.MONDAY],[DayOfWeek.SATURDAY] (不常见),[DayOfWeek.SUNDAY]之一。
     */
    private val startWeekDayLiveData = liveData(DayOfWeek.MONDAY)

    /**
     * mutable | 主界面的Tab页
     */
    private val mainTabLiveData = liveData(MainPageTabs.DAILY_SET)

    /**
     * 数据库seed数据的版本号
     */
    private val seedVersionLiveData = liveData(service.preferenceRepository.seedVersion)

    /**
     * userUid
     */
    private val userUidLiveData = liveData(service.preferenceRepository.currentUserUid)

    /**
     * users
     */
    private val usersLiveData = liveData(service.userRepository.users)

    /**
     * userState
     */
    private val userStateLiveData =
        liveData2Map(usersLiveData, userUidLiveData, "userState") { users, currentUserUid ->
            val currentUser = users.find { it.userUid == currentUserUid } ?: User.default()
            UserState(currentUser, currentUserUid)
        }

    //endregion


    /**
     * mutable | dailyTableUid
     */
    private val dailyTableUidLiveData = liveData(DailyTable.default)
    private val dailyTableSummariesLiveData =
        liveData(service.dailyTableRepository.dailyTableSummaries)
    private val dailyTRCLiveData = liveDataMapAsync(dailyTableUidLiveData, "dailyTRC") {
        service.dailyTableRepository.loadDailyTRC(it)
    }.ignoreNull()
    private val dailyTableState2LiveData =
        liveData2Map(dailyTRCLiveData, userUidLiveData) { value1, value2 ->
            DailyTableState2(value1, value2)
        }

    // ---dailySets
    private val dailySetsLiveData = liveData(service.dailySetRepository.dailySets)
    private val normalDailyDurationsLiveData =
        liveData(service.dailySetRepository.normalDailyDurations)
    private val clazzDailyDurationsLiveData =
        liveData(service.dailySetRepository.clazzDailyDurations)

    //region ---------------------------- data flow -------------------------------------------
    // dailySetUid -----------------> dailySetDurations
    //                    |                  |
    //                    |                  |
    // clazzCursorIndex <-+                  |
    //         |                            |
    //          ----------------------------+-> clazzDailySetCursorsAndIndex --> %ClazzDailySetStatePart%
    //                                                                               ^
    //                                                                               |
    //                                                                               |
    //                                                                               |
    // userUid ----------------------------------------------------------------------+
    //                                                                               |
    // startWeekDay -----------------------------------------------------------------
    //
    //endregion ---------------------------------------------------------------------------------------

    /**
     * mutable, runtimeOnly the cache for clazzDailySetCursorCache
     */
    private val clazzDailySetCursorCache = HashMap<String, Int>()

    private val dailySetUidLiveData = liveData("")
    /**
     * mutable | clazzWeekDay
     */
    private val clazzWeekDayLiveData = liveData(DayOfWeek.MONDAY)
    private val dailySetDurationsLiveData = liveDataChain(dailySetUidLiveData, "dailySetDurations") { value, collector: LiveCollector<DailySetDurations?> ->
        logger.d(Tags.liveDataExtension, "dailySetUid changed to $value")

        // invalid the current cursor.
        clazzCursorIndexLiveData.postValue(null)

        collector.emitSource(liveData(service.dailySetRepository.loadDailySetDurations(value)))
    }.ignoreNull()


    /**
     * {mutable, runtimeOnly} the current cursor, if null, the cursor will be re generated.
     */
    val clazzCursorIndexLiveData = liveData<Int?>(null)

    // --- onlyClazzDailySet
    private val clazzDailySetCursorsAndIndexLiveData: LiveData<Pair<Int, ClazzDailySetCursors>> = liveData2Chain(
        clazzCursorIndexLiveData,
        dailySetDurationsLiveData,
        "clazzDailySetCursors"
    ) { value1, value2, collector ->
        if (value2.dailySet.type == DailySetType.Clazz) {

            val cursors = ClazzDailySetCursors(value2)
            var needReload = false
            val cacheKey = cursors.dailySetDurations.dailySet.uid

            if (clazzDailySetCursorCache[cacheKey] !in cursors.indices) {
                needReload = true
            }

            if (needReload) {
                val index = cursors.findIndex(LocalDate.now())
                clazzDailySetCursorCache[cacheKey] = index
                collector.emit(Pair(index, cursors))

                // side effect.
                // update the clazzWeekDay.
//                clazzWeekDayLiveData.postValue(LocalDate.now().dayOfWeek)
            } else {
                val cursorIndex = value1 ?: (clazzDailySetCursorCache[cacheKey] ?: 0)
                // save the cursor to the cache.
                clazzDailySetCursorCache[cacheKey] = cursorIndex
                collector.emit(Pair(cursorIndex, cursors))
            }
        }
    }

    private val clazzDailySetPagerInfoLiveData = liveDataMap(clazzDailySetCursorsAndIndexLiveData, "clazzDailySetPageLength") {
        val (index, cursors) = it
        PagerInfo(
            size = cursors.size,
            pageIndex = index
        )
    }


    private val clazzDailySetStatePart = ClazzDailySetStatePart(this, clazzDailySetCursorsAndIndexLiveData.asFlow().map { it.second }, userUidLiveData.asFlow(), startWeekDayLiveData.asFlow())
    private val clazzDailySetStateLiveData = liveDataMapAsync(clazzDailySetCursorsAndIndexLiveData) {
        val (index, _) = it
        clazzDailySetStatePart[index]
    }

//    private val clazzDailySetBindingLiveData =
//        liveDataMapAsync(clazzDailySetCursorsLiveData) { cursors ->
//            val cursor = cursors.cursor
//            service.dailySetRepository.loadDailySetBinding(
//                cursors.dailySetDurations.dailySet.uid,
//                cursor.dailyDuration.uid
//            )
//        }
//    private val clazzDailyTRCLiveData = liveDataValueOrDefault(
//        liveDataChain(
//            clazzDailySetBindingLiveData,
//            "clazzDailyTRC"
//        ) { value, collector: LiveCollector<DailyTRC?> ->
//            if (value != null) {
//                collector.emitSource(liveData(service.dailyTableRepository.loadDailyTRC(value.bindingDailyTableUid)))
//            } else {
//                collector.emit(DailyTRC.default())
//            }
//        }, DailyTRC.default()
//    )
//    private val clazzDailyTableState2LiveData = liveData2Map(
//        clazzDailyTRCLiveData,
//        userUidLiveData,
//        "clazzDailyTableState2"
//    ) { value1, value2 ->
//        DailyTableState2(value1, value2)
//    }

//    private val clazzDailySetStateLiveData = liveData3Map(
//        clazzDailySetCursorsLiveData,
//        clazzDailyTableState2LiveData,
//        startWeekDayLiveData,
//        "clazzDailySetState"
//    ) { cursors, dailyTableState2, startDayOfWeek ->
//        ClazzDailySetState(cursors, dailyTableState2, startDayOfWeek, this)
//    }


    //endregion


    //region dailyTable.settings scope

    //region dailySet scope


    //endregion

    //region liveData Exports liveData的导出项
    /**
     * 现在的日期
     */
    val nowDate = liveData(nowDateFlow).initial(LocalDate.ofEpochDay(0))

    val startWeekDay = startWeekDayLiveData.initial(DayOfWeek.MONDAY)

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
    val userUid = userUidLiveData.initial(PreferenceName.CURRENT_USER_UID.defaultValue)

    /**
     * 数据库中记录的用户
     */
    val users = usersLiveData.initial(listOf())

    /**
     * 当前用户状态
     */
    val userState = userStateLiveData.initial(UserState(User.default(), User.local))

    /**
     * 所有DailyTable
     */
    val dailyTableSummaries = dailyTableSummariesLiveData.initial(listOf())

    /**
     * {mutable} 当前DailyTable的Uid
     */
    val dailyTableUid = dailyTableUidLiveData.initial(DailyTable.default)

    /**
     * 当前DailyTable的状态集
     */
    val dailyTableState2 = dailyTableState2LiveData.initial(DailyTableState2.default())

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
     * {mutable} 当前dailySetUid
     */
    val dailySetUid = dailySetUidLiveData.initial("")

    /**
     * 当前DailySetDurations
     */
    val dailySetDurations = dailySetDurationsLiveData.initial(DailySetDurations.empty())

    /**
     * 当前clazzDailySetState
     */
    val clazzDailySetState = clazzDailySetStateLiveData.initial(ClazzDailySetState.empty())

    /**
     * 当前的clazzWeekDay
     */
    val clazzWeekDay = clazzWeekDayLiveData.initial(DayOfWeek.MONDAY)

    val clazzDailySetPagerInfo = clazzDailySetPagerInfoLiveData.initial(PagerInfo.empty())

    fun clazzDailySetStateOfIndex(index: Int): InitialLiveData<ClazzDailySetState> {
        return clazzDailySetStatePart[index].asLiveData().initial(ClazzDailySetState.empty())
    }

    //endregion


    companion object {
        const val TAG = "MainViewModel"
    }


}
package org.tty.dailyset.datasource.runtime

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.asActivityColdStateFlow
import org.tty.dailyset.ui.page.MainPageTabs
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class RuntimeDataSourceImpl(private val sharedComponents: SharedComponents) : RuntimeDataSource {

    private var scheduleJob: Job? = null

    /**
     * 时钟源
     */
    override val now = flow {
        while (true) {
            emit(LocalDateTime.now())
            delay(200)
        }
    }.asActivityColdStateFlow(LocalDateTime.now())

    override val nowDate: Flow<LocalDate> = now.map { it.toLocalDate() }.distinctUntilChanged()

    override val nowDayOfWeek: Flow<DayOfWeek> = nowDate.map { it.dayOfWeek }.distinctUntilChanged()

    override val mainTab = MutableStateFlow(MainPageTabs.SUMMARY)

//    override val rootRoute = MutableStateFlow(MainDestination.INDEX)

    override val currentDailySetUid = MutableStateFlow("")

    /**
     * 初始化
     */
    override fun init() {

//        if (scheduleJob == null) {
//            scheduleJob = sharedComponents.applicationScope.launch {
//                job()
//            }
//            logger.d(TAG, "schedule start")
//        }
//
//        sharedComponents.lifecycle.addObserver(object : LifecycleEventObserver {
//            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//                if (event == Lifecycle.Event.ON_START) {
//                    if (scheduleJob == null) {
//                        scheduleJob = sharedComponents.applicationScope.launch {
//                            job()
//                        }
//                    }
//                    logger.d(TAG, "schedule start")
//                } else if (event == Lifecycle.Event.ON_PAUSE) {
//                    scheduleJob?.cancel()
//                    scheduleJob = null
//                    logger.d(TAG, "schedule stop")
//                }
//            }
//        })

        sharedComponents.applicationScope.launch {
            sharedComponents.repositoryCollection.userRepository.firstLoad()
        }

    }

    /**
     * 每100ms发射的时钟源
     */
//    private suspend inline fun job() {
//        while (true) {
//            val now = LocalDateTime.now()
//            this@RuntimeDataSourceImpl.now.emit(now)
//            //logger.d(TAG, "now is ${now.toShortString()}")
//            delay(100)
//        }
//    }

    companion object {
        const val TAG = "RuntimeDataSourceImpl"
    }
}
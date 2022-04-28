package org.tty.dailyset.datasource.runtime

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.component.common.SharedComponents
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class RuntimeDataSourceImpl(private val sharedComponents: SharedComponents): RuntimeDataSource {

    private var scheduleJob: Job? = null

    /**
     * 时钟源
     */
    override val now = MutableStateFlow<LocalDateTime>(LocalDateTime.now())

    override val nowDate: Flow<LocalDate> = now.map { it.toLocalDate() }.distinctUntilChanged()

    override val nowDayOfWeek: Flow<DayOfWeek> = nowDate.map { it.dayOfWeek }.distinctUntilChanged()

    /**
     * 初始化
     */
    override fun init() {
        sharedComponents.lifecycle.addObserver(object: LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_START) {
                    if (scheduleJob == null) {
                        scheduleJob = sharedComponents.applicationScope.launch {
                            job()
                        }
                    }
                    logger.d(TAG, "schedule start")
                } else if (event == Lifecycle.Event.ON_PAUSE) {
                    scheduleJob?.cancel()
                    scheduleJob = null
                    logger.d(TAG, "schedule stop")
                }
            }
        })
        if (scheduleJob == null) {
            scheduleJob = sharedComponents.applicationScope.launch {
                job()
            }
            logger.d(TAG, "schedule start")
        }
    }

    /**
     * 每100ms发射的时钟源
     */
    private suspend inline fun job() {
        while (true) {
            val now = LocalDateTime.now()
            this@RuntimeDataSourceImpl.now.emit(now)
            //logger.d(TAG, "now is ${now.toShortString()}")
            delay(100)
        }
    }

    companion object {
        const val TAG = "RuntimeDataSourceImpl"
    }
}
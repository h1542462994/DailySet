package org.tty.dailyset.common.datetime

import java.time.LocalTime

data class ExpandTime(
    val dayValue: Int = 0,
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val nano: Int = 0
) {
    fun toPair(): Pair<Int, LocalTime> {
        return Pair(
            this.dayValue,
            toLocalTime()
        )
    }

    fun toLocalTime(): LocalTime {
        return LocalTime.of(
            hour, minute, second, nano
        )
    }

    companion object {
        fun of(localTime: LocalTime): ExpandTime {
            return ExpandTime(
                dayValue = 0,
                hour = localTime.hour,
                minute = localTime.minute,
                second = localTime.second,
                nano = localTime.nano
            )
        }

        fun of(dayValue: Int, localTime: LocalTime): ExpandTime {
            return ExpandTime(
                dayValue = dayValue,
                hour = localTime.hour,
                minute = localTime.minute,
                second = localTime.second,
                nano = localTime.nano
            )
        }
    }
}

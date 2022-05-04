package org.tty.dailyset.converter

import androidx.room.TypeConverter
import org.tty.dailyset.bean.enums.UnicTickStatus

class UnicTicketStatusConverter {
    @TypeConverter
    fun unicTicketStatusToInt(unicTickStatus: UnicTickStatus): Int {
        return unicTickStatus.value
    }

    @TypeConverter
    fun intToUnicTicketStatus(value: Int): UnicTickStatus {
        return UnicTickStatus.values().single { it.value == value }
    }
}
package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.common.datetime.epochLocalDateTime
import org.tty.dailyset.model.converter.StringLocalDateTimeConverter
import java.time.LocalDateTime

@Entity(tableName = "daily_table")
@TypeConverters(StringLocalDateTimeConverter::class)
data class DailyTable(
    @PrimaryKey
    var uid: String,
    var name: String,
    var global: Boolean,
    var referenceUid: String,
    var updateAt: LocalDateTime
) {
    companion object {
        const val default = "#default"

        fun default(): DailyTable {
            return DailyTable(
                uid = default,
                name = "系统默认",
                global = true,
                referenceUid = User.system,
                updateAt = epochLocalDateTime()
            )
        }
    }
}
package org.tty.dailyset.bean.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.common.datetime.epochLocalDateTime
import org.tty.dailyset.converter.DailySetIconConverter
import org.tty.dailyset.converter.DailySetTypeConverter
import org.tty.dailyset.converter.StringLocalDateTimeConverter
import java.time.LocalDateTime

/**
 * represents a dailySet
 */
@Entity(tableName = "daily_set")
@TypeConverters(DailySetTypeConverter::class, DailySetIconConverter::class, StringLocalDateTimeConverter::class)
data class DailySet(
    /**
     * the dailySet type.
     */
    val type: DailySetType = DailySetType.Normal,

    /**
     * the dailySet icon
     */
    val icon: DailySetIcon?,

    /**
     * the unique uid.
     */
    @PrimaryKey
    val uid: String = "",

    val serialIndex: Int = 0,

    /**
     * the ownerUid
     */
    val ownerUid: String = "",

    /**
     * the display name.
     */
    val name: String = "",

    /**
     * the update timestamp
     */
    val updateAt: LocalDateTime
) {
    companion object {
        fun empty(): DailySet {
            return DailySet(
                type = DailySetType.Normal,
                icon = null,
                uid = User.system,
                serialIndex = 0,
                ownerUid = User.system,
                name = "",
                updateAt = epochLocalDateTime()
            )
        }
    }
}
package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.model.converter.DailySetIconConverter
import org.tty.dailyset.model.converter.DailySetTypeConverter
import org.tty.dailyset.model.converter.LongTimeStampConverter
import java.sql.Timestamp

/**
 * represents a dailySet
 */
@Entity(tableName = "daily_set")
@TypeConverters(DailySetTypeConverter::class, DailySetIconConverter::class, LongTimeStampConverter::class)
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
    val updateAt: Timestamp
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
                updateAt = Timestamp(0)
            )
        }
    }
}
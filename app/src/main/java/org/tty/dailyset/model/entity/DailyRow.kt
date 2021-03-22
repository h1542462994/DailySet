package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.model.converter.LongTimeStampConverter
import org.tty.dailyset.model.converter.StringIntArrayConverter
import java.sql.Timestamp

@Entity(tableName = "daily_row")
@TypeConverters(LongTimeStampConverter::class, StringIntArrayConverter::class)
data class DailyRow(
    @PrimaryKey
    var uid: String,
    var weekdays: IntArray,
    var counts: IntArray,
    var updateAt: Timestamp,
    val dailyTableUid: String,
    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DailyRow

        if (uid != other.uid) return false
        if (!weekdays.contentEquals(other.weekdays)) return false
        if (!counts.contentEquals(other.counts)) return false
        if (updateAt != other.updateAt) return false
        if (dailyTableUid != other.dailyTableUid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + weekdays.contentHashCode()
        result = 31 * result + counts.contentHashCode()
        result = 31 * result + updateAt.hashCode()
        result = 31 * result + dailyTableUid.hashCode()
        return result
    }

    companion object {
        const val default = "#default"
        fun default(): DailyRow {
            return DailyRow(
                uid = default,
                weekdays = intArrayOf(1,2,3,4,5,6,7),
                counts = intArrayOf(5,4,3),
                updateAt = Timestamp(0),
                dailyTableUid = DailyTable.default
            )
        }
    }

}
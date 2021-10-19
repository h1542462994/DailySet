package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.common.datetime.epochLocalDateTime
import org.tty.dailyset.model.converter.StringLocalDateTimeConverter
import org.tty.dailyset.model.converter.StringLocalTimeConverter
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(tableName = "daily_cell")
@TypeConverters(StringLocalTimeConverter::class, StringLocalDateTimeConverter::class)
data class DailyCell(
    @PrimaryKey
    var uid: String,
    /**
     * the reference dailyRowUid
     */
    var dailyRowUid: String,
    /**
     * the index of the total.
     */
    var currentIndex: Int,
    /**
     * time duration .start
     */
    var start: LocalTime,
    /**
     * time duration .end
     */
    var end: LocalTime,
    /**
     * a.m. = 0; p.m. = 1; evening = 2
     */
    var normalType: Int,
    /**
     * the index of the current group
     */
    var serialIndex: Int,

    var updateAt: LocalDateTime
): Comparable<DailyCell> {
    companion object {
        const val default = "#default"

        fun default(): List<DailyCell> {
            return listOf(
                DailyCell(
                    uid = "${default}-0",
                    currentIndex = 0,
                    start = LocalTime.parse("08:00:00"),
                    end = LocalTime.parse("08:45:00"),
                    normalType = 0,
                    serialIndex = 0,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-1",
                    currentIndex = 1,
                    start = LocalTime.parse("08:55:00"),
                    end = LocalTime.parse("09:40:00"),
                    normalType = 0,
                    serialIndex = 1,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-2",
                    currentIndex = 2,
                    start = LocalTime.parse("09:55:00"),
                    end = LocalTime.parse("10:40:00"),
                    normalType = 0,
                    serialIndex = 2,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-3",
                    currentIndex = 3,
                    start = LocalTime.parse("10:50:00"),
                    end = LocalTime.parse("11:35:00"),
                    normalType = 0,
                    serialIndex = 3,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-4",
                    currentIndex = 4,
                    start = LocalTime.parse("11:45:00"),
                    end = LocalTime.parse("12:30:00"),
                    normalType = 0,
                    serialIndex = 4,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-5",
                    currentIndex = 5,
                    start = LocalTime.parse("13:30:00"),
                    end = LocalTime.parse("14:15:00"),
                    normalType = 1,
                    serialIndex = 0,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-6",
                    currentIndex = 7,
                    start = LocalTime.parse("14:25:00"),
                    end = LocalTime.parse("15:10:00"),
                    normalType = 1,
                    serialIndex = 1,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-7",
                    currentIndex = 8,
                    start = LocalTime.parse("15:25:00"),
                    end = LocalTime.parse("16:10:00"),
                    normalType = 1,
                    serialIndex = 2,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-8",
                    currentIndex = 9,
                    start = LocalTime.parse("16:20:00"),
                    end = LocalTime.parse("17:05:00"),
                    normalType = 1,
                    serialIndex = 3,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-9",
                    currentIndex = 10,
                    start = LocalTime.parse("18:30:00"),
                    end = LocalTime.parse("19:15:00"),
                    normalType = 2,
                    serialIndex = 0,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-10",
                    currentIndex = 11,
                    start = LocalTime.parse("19:25:00"),
                    end = LocalTime.parse("20:10:00"),
                    normalType = 2,
                    serialIndex = 1,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
                DailyCell(
                    uid = "${default}-11",
                    currentIndex = 12,
                    start = LocalTime.parse("20:20:00"),
                    end = LocalTime.parse("21:05:00"),
                    normalType = 2,
                    serialIndex = 2,
                    dailyRowUid = DailyRow.default,
                    updateAt = epochLocalDateTime()
                ),
            )
        }
    }

    override fun compareTo(other: DailyCell): Int {
        return this.currentIndex.compareTo(other.currentIndex)
    }
}
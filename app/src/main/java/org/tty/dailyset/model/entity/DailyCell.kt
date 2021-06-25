package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.model.converter.LongTimeStampConverter
import org.tty.dailyset.model.converter.StringTimeConverter
import java.sql.Time
import java.sql.Timestamp

@Entity(tableName = "daily_cell")
@TypeConverters(LongTimeStampConverter::class, StringTimeConverter::class)
data class DailyCell(
    @PrimaryKey
    var dailyCellUid: String,
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
    var start: Time,
    /**
     * time duration .end
     */
    var end: Time,
    /**
     * a.m. = 0; p.m. = 1; evening = 2
     */
    var normalType: Int,
    /**
     * the index of the current group
     */
    var serialIndex: Int,

    var updateAt: Timestamp
): Comparable<DailyCell> {
    companion object {
        const val default = "#default"

        fun default(): List<DailyCell> {
            return listOf(
                DailyCell(
                    dailyCellUid = "${default}-0",
                    currentIndex = 0,
                    start = Time.valueOf("08:00:00"),
                    end = Time.valueOf("08:45:00"),
                    normalType = 0,
                    serialIndex = 0,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-1",
                    currentIndex = 1,
                    start = Time.valueOf("08:55:00"),
                    end = Time.valueOf("09:40:00"),
                    normalType = 0,
                    serialIndex = 1,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-2",
                    currentIndex = 2,
                    start = Time.valueOf("09:55:00"),
                    end = Time.valueOf("10:40:00"),
                    normalType = 0,
                    serialIndex = 2,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-3",
                    currentIndex = 3,
                    start = Time.valueOf("10:50:00"),
                    end = Time.valueOf("11:35:00"),
                    normalType = 0,
                    serialIndex = 3,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-4",
                    currentIndex = 4,
                    start = Time.valueOf("11:45:00"),
                    end = Time.valueOf("12:30:00"),
                    normalType = 0,
                    serialIndex = 4,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-5",
                    currentIndex = 5,
                    start = Time.valueOf("13:30:00"),
                    end = Time.valueOf("14:15:00"),
                    normalType = 1,
                    serialIndex = 0,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-6",
                    currentIndex = 7,
                    start = Time.valueOf("14:25:00"),
                    end = Time.valueOf("15:10:00"),
                    normalType = 1,
                    serialIndex = 1,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-7",
                    currentIndex = 8,
                    start = Time.valueOf("15:25:00"),
                    end = Time.valueOf("16:10:00"),
                    normalType = 1,
                    serialIndex = 2,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-8",
                    currentIndex = 9,
                    start = Time.valueOf("16:20:00"),
                    end = Time.valueOf("17:05:00"),
                    normalType = 1,
                    serialIndex = 3,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-9",
                    currentIndex = 10,
                    start = Time.valueOf("18:30:00"),
                    end = Time.valueOf("19:15:00"),
                    normalType = 2,
                    serialIndex = 0,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-10",
                    currentIndex = 11,
                    start = Time.valueOf("19:25:00"),
                    end = Time.valueOf("20:10:00"),
                    normalType = 2,
                    serialIndex = 1,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    dailyCellUid = "${default}-11",
                    currentIndex = 12,
                    start = Time.valueOf("20:20:00"),
                    end = Time.valueOf("21:05:00"),
                    normalType = 2,
                    serialIndex = 2,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
            )
        }
    }

    override fun compareTo(other: DailyCell): Int {
        return this.currentIndex.compareTo(other.currentIndex)
    }
}
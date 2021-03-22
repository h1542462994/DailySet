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
    var uid: String,
    var start: Time,
    var end: Time,
    var normalType: Int,
    var serialIndex: Int,
    var dailyRowUid: String,
    var updateAt: Timestamp
) {
    companion object {
        const val default = "#default"

        fun default(): Iterable<DailyCell> {
            return listOf(
                DailyCell(
                    uid = "${default}-0",
                    start = Time.valueOf("08:00:00"),
                    end = Time.valueOf("08:45:00"),
                    normalType = 0,
                    serialIndex = 0,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-1",
                    start = Time.valueOf("08:55:00"),
                    end = Time.valueOf("09:40:00"),
                    normalType = 0,
                    serialIndex = 1,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-2",
                    start = Time.valueOf("09:55:00"),
                    end = Time.valueOf("10:40:00"),
                    normalType = 0,
                    serialIndex = 2,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-3",
                    start = Time.valueOf("10:50:00"),
                    end = Time.valueOf("11:35:00"),
                    normalType = 0,
                    serialIndex = 3,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-4",
                    start = Time.valueOf("11:45:00"),
                    end = Time.valueOf("12:30:00"),
                    normalType = 0,
                    serialIndex = 4,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-5",
                    start = Time.valueOf("13:30:00"),
                    end = Time.valueOf("14:15:00"),
                    normalType = 1,
                    serialIndex = 0,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-6",
                    start = Time.valueOf("14:25:00"),
                    end = Time.valueOf("15:10:00"),
                    normalType = 1,
                    serialIndex = 1,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-7",
                    start = Time.valueOf("15:25:00"),
                    end = Time.valueOf("16:10:00"),
                    normalType = 1,
                    serialIndex = 2,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-8",
                    start = Time.valueOf("16:20:00"),
                    end = Time.valueOf("17:05:00"),
                    normalType = 1,
                    serialIndex = 3,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-9",
                    start = Time.valueOf("18:30:00"),
                    end = Time.valueOf("19:15:00"),
                    normalType = 2,
                    serialIndex = 0,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-10",
                    start = Time.valueOf("19:25:00"),
                    end = Time.valueOf("20:10:00"),
                    normalType = 2,
                    serialIndex = 1,
                    dailyRowUid = DailyRow.default,
                    updateAt = Timestamp(0)
                ),
                DailyCell(
                    uid = "${default}-11",
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
}
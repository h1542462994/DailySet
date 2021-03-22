package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "daily_table")
data class DailyTable(
    @PrimaryKey
    var uid: String,
    var name: String,
    var global: Boolean,
    var referenceUid: String,
    var updateAt: Timestamp
) {
    companion object {
        const val default = "#default"

        fun default(): DailyTable {
            return DailyTable(
                uid = default,
                name = "系统默认",
                global = true,
                referenceUid = User.system,
                updateAt = Timestamp(0)
            )
        }
    }
}
package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "dailyset_visible", primaryKeys = ["user_uid", "dailyset_uid"])
data class DailySetVisible(
    @ColumnInfo(name = "user_uid")
    val userUid: String,
    @ColumnInfo(name = "dailyset_uid")
    val dailySetUid: String,
    val visible: Boolean
)